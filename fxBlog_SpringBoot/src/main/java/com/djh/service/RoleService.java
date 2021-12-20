package com.djh.service;

import com.djh.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
public interface RoleService extends IService<Role> {

    List<Role> listRolesByUserId(Long userId);

    Long getRoleIdByName(String roleName);
}
