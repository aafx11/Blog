package com.djh.entity;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class Register {
    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private String nickname;

    private String code;

    private String token;
}
