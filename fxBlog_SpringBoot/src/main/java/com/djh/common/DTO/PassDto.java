package com.djh.common.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PassDto {

    @NotBlank(message = "旧密码不能为空")
    private String oldPassWord;
    @NotBlank(message = "新密码不能为空")
    private String newPassWord;
}
