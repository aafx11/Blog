package com.djh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.djh.common.lang.Result;
import com.djh.entity.Message;
import com.djh.entity.SessionList;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RelationSupport;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-09-19
 */
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {

    //进入会话前,获取两个用户之间的消息列表
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/getMessageList/{sessionId}")
    public Result getMessageList(@PathVariable("sessionId") Long sessionId){
        //找到该会话，通过会话里面to和from找到相对应的message
        SessionList sessionList = sessionListService.getOne(new QueryWrapper<SessionList>().eq("id", sessionId));
        //先判断这个会话是否已经被创建
        if (ObjectUtils.isEmpty(sessionList)){
            return Result.success("没有找到该会话");
        }

        //找到双方的聊天记录
        List<Message> messages = messageMapper.selectMessageList(sessionList.getFromUserId(), sessionList.getToUserId());

        //将消息改为已读
        messageService.update(new UpdateWrapper<Message>().eq("from_user_id",sessionList.getToUserId())
                                                        .eq("to_user_id",sessionList.getFromUserId())
                                                        .set("read_flag",1));

        //将会话中未读消息数量设置为0
        sessionListService.update(new UpdateWrapper<SessionList>().eq("from_user_id",sessionList.getFromUserId())
                                                                    .eq("to_user_id",sessionList.getToUserId())
                                                                    .set("un_read_count",0));

        return Result.success(messages);

    }
}
