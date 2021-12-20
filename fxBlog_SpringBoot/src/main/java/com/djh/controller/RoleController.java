package com.djh.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.lang.Const;
import com.djh.common.lang.Result;
import com.djh.entity.Role;
import com.djh.entity.RoleMenu;
import com.djh.entity.UserRole;
import com.djh.service.RoleMenuService;
import com.djh.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 *  角色控制器
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    /**
     * @name: RoleInfo
     * @description:
     * 1.通过role_id获取id详细信息，
     * 2.通过roleMenu关联表获取角色所对应的menuId
     * @param id
     * @return com.djh.common.lang.Result,返回角色详细信息以及该角色所对应的菜单id
     */
    @PreAuthorize("hasAuthority('system:role')")
    @GetMapping("/RoleInfo/{id}")
    public Result RoleInfo(@PathVariable("id") Long id){
        Role role = roleService.getById(id);
        List<RoleMenu> roleMenus = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id",id));//输入的id与RoleMenu表中的role_id相等
        List<Long> menuIds = roleMenus.stream().map(r -> r.getMenuId()).collect(Collectors.toList());//一个角色对应多个菜单id，将这些菜单id收集成menuIds
        role.setMenuIds(menuIds);
        return Result.success(role);
    }

    /**
     * @name: RoleList
     * @description:1.判断是否有输入roleName,有输入才进行模糊查询
     * @param roleName
     * @return com.djh.common.lang.Result返回分页信息
     */
    @PreAuthorize("hasAuthority('system:role')")
    @GetMapping("/RoleList")
    public Result RoleList(String roleName){
        Page<Role> pageData = roleService.page(getPage(),
                new QueryWrapper<Role>().like(StrUtil.isNotBlank(roleName),"role_name",roleName)
        );
        return Result.success(pageData);
    }

    /*传的是表单在body里面*/
    @PreAuthorize("hasAuthority('role:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody Role role){
        role.setCreated(LocalDateTime.now());
        role.setState(role.getState());
        roleService.save(role);
        return Result.success(role);
    }

    /*与save相比，update多了一个id,通过id更新相关信息*/
    @PreAuthorize("hasAuthority('role:update')")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody Role role){
        role.setUpdated(LocalDateTime.now());
        roleService.updateById(role);
        userInfoService.clearRedisAuthorityByRoleId(role.getId());//删除该角色在redis中的相关权限信息缓存
        return Result.success(role);
    }

    /**
     * @name: delete 删除角色
     * @description:
     * 1.批量删除角色，数组转集合
     * 2.删除role与menu和user关联表的相关数据
     * 3.清除redis缓存
     * @param roleIds
     * @return com.djh.common.lang.Result
     */
    @PreAuthorize("hasAuthority('role:delete')")
    @Transactional
    @PostMapping("/delete")
    public Result delete(@RequestBody Long[] roleIds){
        roleService.removeByIds(Arrays.asList(roleIds));
        userRoleService.remove(new QueryWrapper<UserRole>().in("role_id",roleIds));
        roleMenuService.remove(new QueryWrapper<RoleMenu>().in("role_id",roleIds));

        Arrays.stream(roleIds).forEach(id ->{
            userInfoService.clearRedisAuthorityByRoleId(id);
        });

        return Result.success("删除成功");
    }

    /**
     * @name: perm，为角色分配菜单权限
     * @description:
     * 1.将数组菜单id和对应的roleId放入集合
     * 2.清除该角色原对应菜单的信息，重新保存新的角色菜单信息
     * 3.清除redis缓存
     * @param roleId
     * @param menuIds
     * @return com.djh.common.lang.Result
     */
    @Transactional
    @PreAuthorize("hasAuthority('role:perm')")
    @PostMapping("/perm/{roleId}")
    public Result perm(@PathVariable("roleId") Long roleId,@RequestBody Long[] menuIds){

        List<RoleMenu> roleMenus = new ArrayList<>();

        Arrays.stream(menuIds).forEach(menuId ->{
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);

            roleMenus.add(roleMenu);
        });

        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id",roleId));
        roleMenuService.saveBatch(roleMenus);
        System.out.println("获取的"+roleId);
        userInfoService.clearRedisAuthorityByRoleId(roleId);

        return Result.success(menuIds);
    }


}
