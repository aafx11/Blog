package com.djh.security;

import cn.hutool.core.util.StrUtil;
import com.djh.entity.UserInfo;
import com.djh.service.UserInfoService;
import com.djh.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends BasicAuthenticationFilter {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    UserInfoService userInfoService;
    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwt = request.getHeader(jwtUtil.getHeader());
           /*没有jwt的情况，直接跳过，后面如果用户执行需要登录的操作时，会提示先登录*/
        if(StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);
            return;
        }

        Claims claims =jwtUtil.getClaimByToken(jwt);
        System.out.println("token内容"+claims);
        /*token里面没有主体内容*/
        if(claims ==null){
            throw new JwtException("JwtToken异常");
        }
        /*判断token是否已过期*/
        if(jwtUtil.jwtExpired(claims)){
            throw new JwtException("JwtToken已过期");
        }

        /*如果JwtToken没有异常，通过获取token中的主体，获取到账号，通过账号获取到用户密码和权限等信息*/
        String username = claims.getSubject();
        UserInfo userInfo = userInfoService.getByUsername(username);

        System.out.println("获取的username"+username);

//        System.out.println("获取到的用户信息"+userInfoService.getByUsername(username));
//        System.out.println("用户id"+userInfo.getId());

        /*当用户输入账号密码后，会进入UsernamePasswordAuthenticationToken验证，生成Authentication交由AuthenticationManager来进行管理*/
//        System.out.println("获取到的权限"+userDetailService.getUserAuthorityById(userInfo.getId()));
        UsernamePasswordAuthenticationToken token =new  UsernamePasswordAuthenticationToken(username,null,userDetailService.getUserAuthorityById(userInfo.getId()));


        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request,response);
    }


}
