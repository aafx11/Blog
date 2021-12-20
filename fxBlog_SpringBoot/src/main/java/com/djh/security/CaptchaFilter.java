package com.djh.security;

import com.alibaba.druid.util.StringUtils;
import com.djh.common.exception.CaptchaException;
import com.djh.common.lang.Const;
import com.djh.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginFailure loginFailure;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();
        System.out.println("正在访问的url"+url);
        if ("/login".equals(url) && httpServletRequest.getMethod().equals("POST")){
            try{
                validate(httpServletRequest);
            }catch (CaptchaException e){
                /*认证失败转入登录失败处理器*/
                loginFailure.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }

    /*验证码校验
    *   1.验证码和key是否为空，为空直接报异常
    *   2.用户输入的code与redis中按key存储的code相比较
    *   3.在验证码校验正确后，删除原来存储在redis中的验证码
    * */
    private void validate(HttpServletRequest httpServletRequest){
        String code =httpServletRequest.getParameter("code");
        String key =httpServletRequest.getParameter("token");

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(key)){
            throw new CaptchaException("验证码错误");
        }

        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))){
            throw new CaptchaException("验证码错误");
        }

        //一次性使用，删除验证码
        redisUtil.hdel(Const.CAPTCHA_KEY,key);
    }
}
