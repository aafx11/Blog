package com.djh.common.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*DTO是展示层与服务层之间传递数据的对象，返回给前端的菜单信息:
*   菜单信息格式：
*   {
*       name:'Role',
*       title:'角色管理'，
*       icon:'图标',
*       path:'manage/role',
*       component:'manage/role',
*       children:[]
* }
*
*
* */
@Data
public class MenuDto implements Serializable {
    private Long id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<MenuDto> children = new ArrayList<>();

}
