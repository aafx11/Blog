package com.djh.controller;

import com.djh.service.UserInfoService;
import com.djh.common.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public Result test(){
        return Result.success(userInfoService.list());
    }

    @PreAuthorize("hasAnyAuthority('sys:user:list')")
    @GetMapping("/test/pass")
    public Result pass(){

        String password = bCryptPasswordEncoder.encode("123456");/*加密后密码*/
        boolean matches = bCryptPasswordEncoder.matches("123456",password);/*真实密码匹配*/

        System.out.println("匹配"+ matches);

        return Result.success(password);
    }


}
