package com.djh.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.vo.articleVO;
import com.djh.entity.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author djh
 * @since 2021-08-19
 */
public interface FollowMapper extends BaseMapper<Follow> {

    Page<Long> getFollowList(@Param("page") Page<Long> page, @Param("userId") Long UserId);

    Page<Long> getFanList(@Param("page") Page<Long> page, @Param("userId") Long UserId);

}
