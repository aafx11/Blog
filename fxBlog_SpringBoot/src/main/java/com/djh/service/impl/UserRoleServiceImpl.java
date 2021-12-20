package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.djh.entity.Role;
import com.djh.entity.UserRole;
import com.djh.mapper.UserRoleMapper;
import com.djh.service.RoleService;
import com.djh.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
