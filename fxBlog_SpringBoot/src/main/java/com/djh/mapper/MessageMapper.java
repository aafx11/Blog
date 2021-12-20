package com.djh.mapper;

import com.djh.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author djh
 * @since 2021-09-19
 */
public interface MessageMapper extends BaseMapper<Message> {

    List<Message> selectMessageList(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);
}
