package com.djh.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
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
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("role_name")
    @NotBlank(message = "角色名字不能为空")
    private String roleName;

    @NotBlank(message = "角色编码不能为空")
    private String codes;

    /**
     * 描述
     */
    private String describes;

    /*用于存储多个菜单id*/
    @TableField(exist = false)
    private List<Long> menuIds = new ArrayList<>();
}
