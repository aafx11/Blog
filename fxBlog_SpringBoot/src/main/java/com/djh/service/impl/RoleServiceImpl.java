package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.djh.entity.Role;
import com.djh.mapper.RoleMapper;
import com.djh.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> listRolesByUserId(Long userId) {

        List<Role> roles = this.list(new QueryWrapper<Role>().inSql("id","select role_id from user_role where user_id = " + userId));

        return roles;
    }

    @Override
    public Long getRoleIdByName(String roleName){
        Role role = getOne(new QueryWrapper<Role>().eq("role_name",roleName));
        return role.getId();
    }
}
