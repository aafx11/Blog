package com.djh.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.djh.common.lang.Result;
import com.djh.common.vo.SessionListVO;
import com.djh.common.vo.UserProfileVO;
import com.djh.entity.RoleMenu;
import com.djh.entity.SessionList;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
@RequestMapping("/sessionList")
public class SessionListController extends BaseController {

    //创建会话
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/createSession")
    public Result createSession(@RequestBody SessionList session){

        //1.创建会话
        SessionList sessionList = new SessionList();
        sessionList.setId(randomUtil.getRandom());
        sessionList.setFromUserId(session.getFromUserId());
        sessionList.setToUserId(session.getToUserId());
        sessionList.setUnReadCount(0);
        sessionListService.save(sessionList);

        //2.创建对方与自己的会话
        //先检查是否已经创建会话
        SessionList one = sessionListService.getOne(new LambdaQueryWrapper<SessionList>()
                .eq(SessionList::getFromUserId, session.getToUserId())
                .eq(SessionList::getToUserId, session.getFromUserId())
        );
        if (ObjectUtils.isEmpty(one)) {
            //当检查到对方与自己没有创建会话
            SessionList sessionList1 = new SessionList();
            sessionList1.setId(randomUtil.getRandom());
            sessionList1.setFromUserId(session.getToUserId());
            sessionList1.setToUserId(session.getFromUserId());
            sessionList1.setUnReadCount(0);
            sessionListService.save(sessionList1);
        }
        return Result.success("成功创建会话",sessionList);
    }


    //获取该用户和其他用户已经创建的会话
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/getSessionListAlready/{fromUserId}")
    public Result getSessionListAlready(@PathVariable("fromUserId") Long fromUserId){
        List<SessionList> sessionList = sessionListService.list(new QueryWrapper<SessionList>().eq("from_user_id", fromUserId));
        List<SessionListVO> sessionListVOS = new ArrayList<>();

        sessionList.forEach(receive ->{
            SessionListVO sessionListVO = new SessionListVO();
            BeanUtils.copyProperties(receive,sessionListVO);
            UserProfileVO userProfile = userInfoService.getUserProfileById(receive.getToUserId());
            sessionListVO.setUsername(userProfile.getUsername());
            sessionListVO.setAvatar(userProfile.getAvatar());
            sessionListVO.setNickname(userProfile.getNickname());
            sessionListVOS.add(sessionListVO);
        });

        return Result.success(sessionListVOS);
    }

    //删除会话
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/deleteSession")
    public Result deleteSession(@RequestBody Long sessionId){

        boolean b = sessionListService.removeById(sessionId);
        if (b){
            return Result.success("删除成功");
        } else {
            return Result.fail("删除失败");
        }

    }

    //获取未读信息数量
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/getUnReadMessageCount")
    public Result getUnReadMessageCount(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int sum = 0;
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            sum = sessionListService.countUnReadMessage();
        } else {
            return Result.fail("您尚未登陆");
        }
        return Result.success(sum);
    }
}
