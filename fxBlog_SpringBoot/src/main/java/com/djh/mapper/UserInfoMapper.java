package com.djh.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.vo.VideoVO;
import com.djh.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    List<Long> getNavMenuByUserId(Long userId);

    List<UserInfo> listByMenuId(Long menuId);

    Page<UserInfo> selectUserOrderByScore(@Param("page") Page<UserInfo> page);
}
