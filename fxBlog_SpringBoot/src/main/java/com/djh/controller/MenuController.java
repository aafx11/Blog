package com.djh.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.djh.common.DTO.MenuDto;
import com.djh.common.lang.Result;
import com.djh.entity.Menu;
import com.djh.entity.RoleMenu;
import com.djh.entity.UserInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * <p>
 *  菜单控制器
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

    /*获取当前用户的菜单信息：
    * Principal是一个包含用户的标识和用户的所属角色的对象
    * */
    @GetMapping("/getNav")
    public Result getNav(Principal principal){
        //通过principal获取到完整的用户信息
        UserInfo userInfo = userInfoService.getByUsername(principal.getName());

        //获取权限信息
        String authorityInfo=userInfoService.getUserAuthority(userInfo.getId());//获取到信息是用逗号分割的，需要转化成数组
        String[] authorityArray= StringUtils.tokenizeToStringArray(authorityInfo,",");

        //获取当前用户的导航栏信息
        List<MenuDto> navs = menuService.getUserNav();

        return Result.success(MapUtil.builder()
                .put("authoritys",authorityArray)
                .put("nav",navs)
                .map()
        );
    }


    /*获取菜单详细信息：
    *    id从url中获取
    *
    * */
    @GetMapping("/MenuInfo/{id}")
    @PreAuthorize("hasAnyAuthority('system:menu')")
    public Result info(@PathVariable(name = "id") Long id){
//        System.out.println("获取的数据"+menuService.getById(id));
        return Result.success(menuService.getById(id));
    }

    /*获取所有菜单的相信信息：
    *    返回树状结构，方便前端表格显示
    *
    * */
    @GetMapping("/MenuList")
    @PreAuthorize("hasAnyAuthority('system:menu')")
    public Result list(){
        List<Menu> menus = menuService.tree();

        return Result.success(menus);
    }

    /*
    * @Validated检验实体
    *
    * */
    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('menu:save')")
    public Result save(@Validated @RequestBody Menu menu){
//        System.out.println("获取到的菜单数据"+menu);
        menu.setCreated(LocalDateTime.now());
        menuService.save(menu);
        return Result.success(menu);
    }


    /**
     * @name: update更新菜单
     * @description:更新菜单后，需要删除该菜单在redis中的权限信息缓存
     * @param menu
     * @return com.djh.common.lang.Result
     */
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('menu:update')")
    public Result update(@Validated @RequestBody Menu menu){
        menu.setUpdated(LocalDateTime.now());
        menuService.updateById(menu);
        userInfoService.clearRedisAuthorityByMenuId(menu.getId());
        return Result.success(menu);


    }

    /**
     * @name: detele删除菜单
     * @description:
     * 1.判断需要删除的菜单是否是父级菜单，删除父级菜单前需要先删除其子菜单，不能直接删除父级菜单
     * 2.删除该菜单在redis中的权限信息缓存
     * 3.删除角色与菜单关联表中该菜单的相关信息
     * 4.删除菜单
     * @param id
     * @return com.djh.common.lang.Result
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('menu:delete')")
    public Result delete(@PathVariable("id") Long id){

        int count=menuService.count(new QueryWrapper<Menu>().eq("parent_id",id));
        if (count > 0) {
            return Result.fail("删除父级菜单前，需要删除其所有子菜单");
        }
        userInfoService.clearRedisAuthorityByMenuId(id);
        menuService.removeById(id);
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id",id));
        return Result.success("删除成功");
    }
}
