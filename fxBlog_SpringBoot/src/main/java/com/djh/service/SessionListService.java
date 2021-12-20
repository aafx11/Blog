package com.djh.service;

import com.djh.entity.SessionList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-09-19
 */
public interface SessionListService extends IService<SessionList> {
     Boolean updateUnReadCount(Long fromUserId,Long toUserId);

     List<SessionList> getSessionListByFromUserId (Long fromUserId);

     SessionList getSessionList(Long fromUserId,Long toUserId);

     Integer countUnReadMessage();
}
