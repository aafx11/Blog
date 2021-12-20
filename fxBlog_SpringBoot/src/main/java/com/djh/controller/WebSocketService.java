package com.djh.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.djh.common.vo.SessionListVO;
import com.djh.common.vo.UserProfileVO;
import com.djh.entity.Message;
import com.djh.entity.SessionList;
import com.djh.entity.UserInfo;
import com.djh.service.MessageService;
import com.djh.service.SessionListService;
import com.djh.service.UserInfoService;
import com.djh.utils.CurrentPool;
import com.djh.utils.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
@ServerEndpoint("/webSocket/{userId}/{sessionId}")
public class WebSocketService {
    private static SessionListService sessionListService;

    @Autowired
    public void setSessionListService(SessionListService sessionListService) {
        WebSocketService.sessionListService = sessionListService;
    }

    private static UserInfoService userInfoService;

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        WebSocketService.userInfoService = userInfoService;
    }

    private static MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        WebSocketService.messageService = messageService;
    }


    //通过session推送消息给指定用户
    private Session session;


    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") Long userId, @PathParam(value = "sessionId") Long sessionId) {
        //将局部session对象赋值给成员session
        this.session = session;
        CurrentPool.webSocket.put(userId, this);//用户id与webSocket相对应
        List<Object> list = new ArrayList<>();
        list.add(sessionId);
        list.add(session);
        CurrentPool.sessionPool.put(userId, list);//用户id对应sessionId和session对象




        System.out.println("【websocket消息】有新的连接，总数为:" + CurrentPool.webSocket.size());
    }

    //推送消息给所有在线用户
/*    private void broadcastAllUsers(String message){
        try{
            //获取所有nickname
            Set<String> nicknames = onlineUsers.keySet();
            for (String nickname : nicknames ){
                //获取每个在线用户的webSocketService
                WebSocketService webSocketService = onlineUsers.get(nickname);
//                webSocketService.session.getBasicRemote().sendText(message);
                webSocketService.session.getAsyncRemote().sendText(message);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }*/

    @OnMessage
    //接到客户端发送消息时
    public void onMessage(String message) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            //获取到前端发送的消息，并转换成对象
//            Message messageObject = objectMapper.readValue(message,Message.class);
//            //转化成推送给指定用户的数据格式
//            String messageData = MessageUtil.getMessageData(false, messageObject.getFromName(), messageObject.getMessage());
//            //通过session发送信息给指定用户
//            onlineUsers.get(messageObject.getToName()).session.getAsyncRemote().sendText(messageData);
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }

        //1.获取sessionId
        Long sessionId = Long.parseLong(this.session.getRequestParameterMap().get("sessionId").get(0));
        if (sessionId == null) {
            System.out.println("sessionId错误");
            return;
        }
        //2.通过获取会话session,来获取用户信息
        SessionList sessionList = sessionListService.getById(sessionId);
        UserInfo fromUserInfo = userInfoService.getById(sessionList.getFromUserId());
        UserInfo toUserInfo = userInfoService.getById(sessionList.getToUserId());

        //3.封装message
        System.out.println("封装消息");
        Message messageInfo = new Message();
        messageInfo.setId(RandomUtil.getRandom());
        messageInfo.setFromUserId(fromUserInfo.getId());
        messageInfo.setFromNickname(fromUserInfo.getNickname());
        messageInfo.setFromAvatar(fromUserInfo.getAvatar());
        messageInfo.setToUserId(toUserInfo.getId());
        messageInfo.setToNickname(toUserInfo.getNickname());
        messageInfo.setContent(message);
        messageInfo.setCreated(LocalDateTime.now());
        messageInfo.setReadFlag(0);//未读
        //消息保存到数据库
        messageService.save(messageInfo);

        //4.判断用户是否在线
        List<Object> list = CurrentPool.sessionPool.get(sessionList.getToUserId());
        if (list == null || list.isEmpty()) {
            //对方不在线
            System.out.println("找不到list" + list);

            //判断对方是否删除了该会话
            SessionList one = sessionListService.getSessionList(sessionList.getToUserId(), sessionList.getFromUserId());
            System.out.println("判断是否为空"+ObjectUtils.isEmpty(one));
            if (ObjectUtils.isEmpty(one)) {
                //对方不在线，且删除了该会话,新建会话，设置未读数为1，不需要推送会话列表
                System.out.println("对方不在线，且删除了该会话,新建会话");
                SessionList newSessionList = new SessionList();
                newSessionList.setId(RandomUtil.getRandom());
                newSessionList.setFromUserId(sessionList.getToUserId());
                newSessionList.setToUserId(sessionList.getFromUserId());
                newSessionList.setUnReadCount(1);
                sessionListService.save(newSessionList);
            } else {
                //对方不在线，但是没有删除该会话，增加该会话的未读信息数量
                sessionListService.updateUnReadCount(sessionList.getToUserId(), sessionList.getFromUserId());
            }

        } else {

            //用户在线时,判断对方的会话是否存在
            SessionList one = sessionListService.getSessionList(sessionList.getToUserId(), sessionList.getFromUserId());
            if (ObjectUtils.isEmpty(one)) {
                System.out.println("对方在线，但是会话不存在，先创建,再推送新的会话列表让对方更新");

                //对方在线，但是会话不存在，先创建
                SessionList newSessionList = new SessionList();
                newSessionList.setId(RandomUtil.getRandom());
                newSessionList.setFromUserId(sessionList.getToUserId());
                newSessionList.setToUserId(sessionList.getFromUserId());
                newSessionList.setUnReadCount(1);
                sessionListService.save(newSessionList);

                //会话不存在发送列表消息,让接收方更新会话列表
                List<SessionList> sessionLists1 = sessionListService.getSessionListByFromUserId(sessionList.getToUserId());

                List<SessionListVO> sessionListVOS = new ArrayList<>();

                sessionLists1.forEach(receive ->{
                    SessionListVO sessionListVO = new SessionListVO();
                    BeanUtils.copyProperties(receive,sessionListVO);
                    UserProfileVO userProfile = userInfoService.getUserProfileById(receive.getToUserId());
                    sessionListVO.setUsername(userProfile.getUsername());
                    sessionListVO.setAvatar(userProfile.getAvatar());
                    sessionListVO.setNickname(userProfile.getNickname());
                    sessionListVOS.add(sessionListVO);
                });



                sendMessageToUser(sessionList.getToUserId(), JSONUtil.toJsonStr(sessionListVOS));
            } else {
                //对方在线，会话存在，判断对方是否在该会话中
                String oneId=one.getId()+ "";
                System.out.println("oneId"+oneId);

                String o = list.get(0)+"";
                System.out.println("list里面的id"+o);

                if (oneId.equals(o) ){
                    System.out.println("对方正在该会话中,之间推送消息");
                    //对方正在该会话中,之间推送消息
                    sendMessageToUser(sessionList.getToUserId(), JSONUtil.toJsonStr(messageInfo));
                }else {
                    //该会话存在，但对方不在该会话中,更新未读信息数，推送新的会话列表
                    System.out.println("对方在线,该会话存在，但对方不在该会话中,更新未读信息数，推送新的会话列表");
                    sessionListService.updateUnReadCount(sessionList.getToUserId(),sessionList.getFromUserId());
                    List<SessionList> sessionLists2 = sessionListService.getSessionListByFromUserId(sessionList.getToUserId());


                    List<SessionListVO> sessionListVOS = new ArrayList<>();

                    sessionLists2.forEach(receive ->{
                        SessionListVO sessionListVO = new SessionListVO();
                        BeanUtils.copyProperties(receive,sessionListVO);
                        UserProfileVO userProfile = userInfoService.getUserProfileById(receive.getToUserId());
                        sessionListVO.setUsername(userProfile.getUsername());
                        sessionListVO.setAvatar(userProfile.getAvatar());
                        sessionListVO.setNickname(userProfile.getNickname());
                        sessionListVOS.add(sessionListVO);
                    });



                    sendMessageToUser(sessionList.getToUserId(), JSONUtil.toJsonStr(sessionListVOS));

//                    sendMessageToUser(sessionList.getToUserId(), JSONUtil.toJsonStr(sessionLists));
                }
            }
    }


        System.out.println("【websocket消息】收到客户端消息:"+message);
}


    @OnClose
    public void onClose() {
        System.out.println("用户id" + this.session.getRequestParameterMap().get("userId"));
        Long userId = Long.parseLong(this.session.getRequestParameterMap().get("userId").get(0));
        CurrentPool.webSocket.remove(userId);
        CurrentPool.sessionPool.remove(userId);

        System.out.println("【websocket消息】连接断开，总数为:" + CurrentPool.webSocket.size());
    }


    //用户之间发送消息,发送的消息为json格式
    public void sendMessageToUser(Long toUserId, String message) {
        //检查收消息的用户是否在线,0为sessionId，1为session对象
        Session session = (Session) CurrentPool.sessionPool.get(toUserId).get(1);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


























