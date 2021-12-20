package com.djh.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父级菜单id，若为1级菜单，值为0
     */
    @NotNull(message = "父级菜单不能为空")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    /**
     * 菜单url路径
     */
    private String path;

    /**
     * 授权
     */
    @NotBlank(message = "授权码不能为空")
    private String perms;

    /**
     * vue组件
     */
    private String component;

    /**
     * 类型，0：1级目录 1：目录下的菜单 2：按钮
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    @TableField("orderNum")
    private Integer orderNum;

    /*数据库中并没有该字段，但是用来表示父级和子级*/
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();


}
