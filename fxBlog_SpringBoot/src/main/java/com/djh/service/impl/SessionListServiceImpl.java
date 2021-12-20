package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.djh.entity.SessionList;
import com.djh.mapper.SessionListMapper;
import com.djh.service.SessionListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djh.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-09-19
 */
@Service
public class SessionListServiceImpl extends ServiceImpl<SessionListMapper, SessionList> implements SessionListService {

    @Autowired
    SessionListService sessionListService;

    @Autowired
    UserInfoService userInfoService;

    public Boolean updateUnReadCount(Long fromUserId, Long toUserId) {

        boolean update = sessionListService.update(new UpdateWrapper<SessionList>()
                .eq("from_user_id", fromUserId)
                .eq("to_user_id", toUserId)
                .setSql("un_read_count=un_read_count+1"));
        return update;
    }

    public List<SessionList> getSessionListByFromUserId(Long fromUserId) {
        List<SessionList> SessionList = sessionListService.list(new QueryWrapper<SessionList>().eq("from_user_id", fromUserId));
        return SessionList;
    }

    //获取一个会话
    public SessionList getSessionList(Long fromUserId, Long toUserId) {
        SessionList one = sessionListService.getOne(new QueryWrapper<SessionList>()
                .eq("from_user_id", fromUserId)
                .eq("to_user_id", toUserId)
        );
        return one;
    }


    //计算未读信息数量
    public Integer countUnReadMessage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userInfoService.getByUsername(auth.getName()).getId();
        List<SessionList> sessionList = sessionListService.list(new QueryWrapper<SessionList>().eq("from_user_id", userId));
        Integer total = 0;

        if(sessionList!=null &&  !sessionList.isEmpty()){
            List<Integer> sum = new ArrayList<>();
            sessionList.forEach(session -> {
                sum.add(session.getUnReadCount());
            });

            for (Integer count : sum){
                total = total + count;
            }
        }

        return total;
    }
}
