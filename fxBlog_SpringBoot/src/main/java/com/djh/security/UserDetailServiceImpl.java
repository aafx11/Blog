package com.djh.security;

import com.djh.entity.UserInfo;
import com.djh.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /*通过username查出完整的用户信息*/
        UserInfo userInfo = userInfoService.getByUsername(username);
        if(userInfo == null){
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return new AccountUser(userInfo.getId(),userInfo.getUsername(),userInfo.getPassword(),getUserAuthorityById(userInfo.getId()));

    }

    /*获取用户权限信息，包括用户角色信息和菜单权限:
    *   1.获取到用户的权限信息，格式:ROLE_admin,sys:user:list
    *   .使用AuthorityUtils工具，将获取到的权限封装成GrantedAuthority格式，AuthorityUtils工具要求用逗号隔开
    *
    * */
    public List<GrantedAuthority> getUserAuthorityById (Long userId){
        String authority = userInfoService.getUserAuthority(userId);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);






    }
}
