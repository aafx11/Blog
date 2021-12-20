package com.djh.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.util.StringUtils;
import com.djh.common.exception.CaptchaException;
import com.djh.common.lang.Const;
import com.djh.common.lang.Result;
import com.djh.entity.Register;
import com.djh.entity.UserInfo;
import com.djh.entity.UserRole;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
public class AuthController extends BaseController {
    @Autowired
    private Producer producer;

    /**
     * 生成验证码图片和key
     *
     */
    @GetMapping("/getCodeImg")
    public Result captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = producer.createText();
        String key = UUID.randomUUID().toString();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);// 存储到redis中
        log.info("key -- {} ---验证码- {}", key, code);
        System.out.println("验证码"+code);
        return Result.success(
                MapUtil.builder()
                        .put("token", key)
                        .put("codeImg", base64Img)
                        .build());


    }

    /*获取顶部用户信息，但不返回密码等敏感信息*/
    @GetMapping("/getUserInfo")
    public Result getUserInfo(Principal principal){
        UserInfo userInfo =userInfoService.getByUsername(principal.getName());

        return Result.success(MapUtil.builder()
                .put("id",userInfo.getId())
                .put("username",userInfo.getUsername())
                .put("avatar",userInfo.getAvatar())
                .put("created",userInfo.getCreated())
                .map()
        );
    }

    @PostMapping("/register")
    public Result register(@Validated @RequestBody Register register){
        String code = register.getCode();
        String token = register.getToken();
        UserInfo username = userInfoService.getByUsername(register.getUsername());
        UserInfo nickname =userInfoService.getByNickname(register.getNickname());

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(token)){
            throw new CaptchaException("验证码错误");
        }

        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,token))){
            throw new CaptchaException("验证码错误");
        }

        if (username != null) {
            return Result.fail("该用户名已被使用");
        }
        if (nickname != null) {
            return Result.fail("该昵称已被使用");
        }



        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(register.getUsername());
        userInfo.setNickname(register.getNickname());
        userInfo.setPassword(bCryptPasswordEncoder.encode(register.getPassword()));
        userInfo.setEmail(register.getEmail());
        userInfo.setState(Const.STATE_ON);
        userInfo.setCreated(LocalDateTime.now());


        if (userInfoService.save(userInfo)) {
            UserInfo user = userInfoService.getByUsername(userInfo.getUsername());
            Long userId = user.getId();
            Long roleId = roleService.getRoleIdByName("普通用户");
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleService.save(userRole);
        }


        redisUtil.hdel(Const.CAPTCHA_KEY,token);
        return Result.success("注册成功");
    }
}