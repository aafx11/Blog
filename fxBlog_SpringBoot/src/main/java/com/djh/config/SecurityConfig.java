package com.djh.config;

import com.djh.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
 *
 * 1.登录配置
 * 2.拦截处理
 * 3.禁用session
 *
 *
 * */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    LoginFailure loginFailure;
    @Autowired
    LoginSuccess loginSuccess;
    @Autowired
    CaptchaFilter captchaFilter;
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    LogoutSuccess logoutSuccess;

    @Bean
    JwtFilter jwtFilter() throws Exception {
        JwtFilter jwtFilter = new JwtFilter(authenticationManager());
        return jwtFilter;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static final String[] URL_WHITELIST = {"/webjars/**", "/favicon.ico", "/getCodeImg", "/login", "/logout", "/test/**", "/register", "/information/getNotices", "/article/articleList",
            "/article/getArticleById","/upload/articleImg","/tag/**","/article/articleListByTag","/comment/getCommentList","/article/ArticleListByLikeSearch",
            "/follow/hasFollow/**","/commentform/getCommentList","/articleLike/likewise","/articleCollect/collect","/ws/**","/webSocket/**","/videoInfo/getVideoDetailByVideoId",
            "/videoInfo/getVideoByStateOrLike","/videoInfo/getVideoOrderByView","/videoInfo/getVideoOrderByCreated","/videoLike/**","/videoCollect/**","/user/getUserProfileVOById"
            ,"/static/**","/videoComment/**","/videoInfo/getVideoByTag","/tagsVideo/getVideoTags","/article/getArticleByUserId","/follow/getFollowListByUserId","/follow/getFansListByUserId",
            "/user/getRandomUsers","/commentform/getLatestComments"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .formLogin()
                .successHandler(loginSuccess)
                .failureHandler(loginFailure)
                .and()/*退出设置*/
                .logout()
                .logoutSuccessHandler(logoutSuccess)
                .and()/*session管理*/
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()/*拦截规则*/
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()

                .and()/*异常处理*/
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)/*进入点*/
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()/*自定义过滤器*/
                .addFilter(jwtFilter())
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);/*在登录处理器之前，插入验证码处理器*/
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}