package com.djh.service;

import com.djh.common.vo.UserProfileVO;
import com.djh.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
public interface UserInfoService extends IService<UserInfo> {
    UserInfo getByUsername(String username);

    UserInfo getByNickname(String nickname);

    String getUserAuthority(Long userId);

    UserProfileVO getUserProfileById(Long userId);

    /*当权限信息发生改变后，应当删除原来存储在redis中的权限信息*/
    void clearRedisAuthority(String username);
    /*当用户角色发生改变时，删除原存储在redis中的权限信息*/
    void clearRedisAuthorityByRoleId(Long roleId);
    /*当角色的菜单发生改变时，删除原存储在redis中的权限信息*/
    void clearRedisAuthorityByMenuId(Long menuId);
}
