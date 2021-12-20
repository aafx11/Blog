package com.djh.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.djh.common.DTO.MenuDto;
import com.djh.entity.Menu;
import com.djh.entity.UserInfo;
import com.djh.mapper.MenuMapper;
import com.djh.mapper.UserInfoMapper;
import com.djh.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djh.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserInfoMapper userInfoMapper;


    /*获取用户菜单信息：
    *   一.获取用户菜单信息
    *       1.通过Security获取当前用户的用户名
    *       2.通过用户名获取到用户完整信息
    *       3.通过用户id获取菜单id，通过菜单id获取完整菜单信息
    *   二.将获取到的信息转换成所需格式
    *       1.转换成所需的树状格式
    *       2.树状结构转换成MenuDto
    * */
    @Override
    public List<MenuDto> getUserNav() {
        /*获取用户菜单信息*/
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo userInfo =userInfoService.getByUsername(username);

        List<Long> menuIds = userInfoMapper.getNavMenuByUserId(userInfo.getId());

        List<Menu> menus = this.listByIds(menuIds);

        //树状结构
        List<Menu> menuTree = BuildMenuTree(menus);



        //树状结构转menuDto
        return covert(menuTree);
    }


    private List<MenuDto> covert(List<Menu> menuTree) {
        List<MenuDto> menuDtos = new ArrayList<>();
        menuTree.forEach(m ->{
            MenuDto dto = new MenuDto();
            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getMenuName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());

            if (m.getChildren().size() > 0){
                dto.setChildren(covert(m.getChildren()));
            }

            menuDtos.add(dto);
        });
        return menuDtos;
    }


    /**
     * @name: BuildMenuTree
     * @description:
     * @param menus
     * @return java.util.List<com.djh.entity.Menu>
     */
    private List<Menu> BuildMenuTree(List<Menu> menus) {
        List<Menu> finalMenus = new ArrayList<>();

        /*循环所有节点，给每个节点都找到自己的子节点*/
        for (Menu menu :menus){
            for (Menu child : menus){
                if (menu.getId() == child.getParentId()){
                    menu.getChildren().add(child);
                }
            }
            if (menu.getParentId()==0){
                finalMenus.add(menu);
            }
        }


//        System.out.println(JSONUtil.toJsonStr(finalMenus));
        return finalMenus;
    }

    /*获取全部菜单详细信息并转化为树状结构：
    *   1.获取所有菜单信息,new QueryChainWrapper<>()查询条件,<>内是实体
    *   2.转化为树状结构
    * */
    @Override
    public List<Menu> tree() {
       List<Menu> menus = this.list(new QueryWrapper<Menu>().orderByAsc("orderNum"));



    return BuildMenuTree(menus);
    }
}
