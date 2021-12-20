package com.djh.entity;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 * 
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
@Data
public class UserInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

//    @TableId(value = "id" , type = IdType.AUTO)
//    private Long id;
//    private LocalDateTime created;
//    private LocalDateTime updated;
//
//    private Integer state;



    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    private String avatar;

    private String briefintroduction;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private String gender;

    @TableField("lastLogin")
    private LocalDateTime lastLogin;

    private String phone;


    private String nickname;

    //用户积分
    private Integer score ;

    private String  avatarBase64;

    @TableField(exist = false)
    private List<Role> roles = new ArrayList<>();
}
