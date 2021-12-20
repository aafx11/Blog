package com.djh.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.DTO.PassDto;
import com.djh.common.lang.Const;
import com.djh.common.lang.Result;
import com.djh.common.vo.UserProfileVO;
import com.djh.entity.*;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
@RestController
@RequestMapping("/user")
public class UserInfoController extends BaseController {


    /**
     * @param id
     * @return com.djh.common.lang.Result返回用户信息，以及用户的角色
     * @name: UserInfo
     * @description: 1.通过用户id搜索用户信息
     * 2.通过用户id搜索相关角色信息
     */
    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping("/UserInfo/{id}")
    public Result UserInfo(@PathVariable("id") Long id) {

        UserInfo userInfo = userInfoService.getById(id);
        Assert.notNull(userInfo, "搜索不到该用户");
        List<Role> roles = roleService.listRolesByUserId(id);
        userInfo.setRoles(roles);
        return Result.success(userInfo);
    }

    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping("/UserList")
    public Result list(String username) {
        Page<UserInfo> pageData = userInfoService.page(getPage(), new QueryWrapper<UserInfo>()
                .like(StrUtil.isNotBlank(username), "username", username));

        pageData.getRecords().forEach(u -> {
            u.setRoles(roleService.listRolesByUserId(u.getId()));
        });
        return Result.success(pageData);
    }

    @PreAuthorize("hasAuthority('user:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody UserInfo userInfo) {

        userInfo.setCreated(LocalDateTime.now());
        userInfo.setState(userInfo.getState());

        String password = bCryptPasswordEncoder.encode(Const.DEFAULT_PASSWORD);
        userInfo.setPassword(password);

        userInfo.setAvatar(Const.DEFAULT_AVATAR);
        userInfoService.save(userInfo);

        return Result.success(userInfo);
    }

    @PreAuthorize("hasAuthority('user:update')")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody UserInfo userInfo) {

        userInfo.setUpdated(LocalDateTime.now());
        userInfoService.updateById(userInfo);
        return Result.success(userInfo);
    }

    //删除用户
    @PreAuthorize("hasAuthority('user:delete')")
    @Transactional
    @PostMapping("/delete")
    public Result delete(@RequestBody Long[] userIds) {

        userInfoService.removeByIds(Arrays.asList(userIds));
        userRoleService.remove(new QueryWrapper<UserRole>().in("user_id", userIds));
        return Result.success("删除成功");
    }

    @PreAuthorize("hasAuthority('user:perm')")
    @Transactional
    @PostMapping("/perm/{userId}")
    public Result perm(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {

        List<UserRole> userRoles = new ArrayList<>();

        Arrays.stream(roleIds).forEach(r -> {
            UserRole userRole = new UserRole();
            userRole.setRoleId(r);
            userRole.setUserId(userId);

            userRoles.add(userRole);
        });

        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));
        userRoleService.saveBatch(userRoles);

        UserInfo userInfo = userInfoService.getById(userId);
        userInfoService.clearRedisAuthority(userInfo.getUsername());
        return Result.success("修改成功");
    }

    @PreAuthorize("hasAuthority('user:respass')")
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody Long userId) {
        UserInfo userInfo = userInfoService.getById(userId);

        userInfo.setPassword(bCryptPasswordEncoder.encode(Const.DEFAULT_PASSWORD));
        userInfo.setUpdated(LocalDateTime.now());
        userInfoService.updateById(userInfo);
        return Result.success("重置密码成功");
    }

    /**
     * @param passDto
     * @param principal
     * @return com.djh.common.lang.Result
     * @name: updatePassWord, 用户修改自己的密码，不需要设置权限
     * @description: 1.通过用户名获取该用户信息，知道这是哪个用户
     * 2.匹配输入的旧密码与用户的真实密码
     * 3.设置新密码
     */
    @PostMapping("/updatePassWord")
    public Result updatePassWord(@Validated @RequestBody PassDto passDto, Principal principal) {
        UserInfo userInfo = userInfoService.getByUsername(principal.getName());
        boolean match = bCryptPasswordEncoder.matches(passDto.getOldPassWord(), userInfo.getPassword());
        if (!match) {
            return Result.fail("请输入正确的密码");
        }

        userInfo.setPassword(bCryptPasswordEncoder.encode(passDto.getNewPassWord()));
        userInfo.setUpdated(LocalDateTime.now());
        userInfoService.updateById(userInfo);

        return Result.success("修改密码成功");
    }


    //编辑个人资料时获取用户信息
    @GetMapping("/getUserInfo")
    public Result getUserInfo() {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserProfileVO userProfileVO = new UserProfileVO();
            UserInfo userInfo = userInfoService.getByUsername(auth.getName());

            BeanUtils.copyProperties(userInfo, userProfileVO);

            userProfileVO.setUserId(userInfo.getId());

            //计算作者用户的文章总数
            int articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getUserId, userInfo.getId()));
            userProfileVO.setArticleCount(articleCount);

            //计算作者用户的粉丝总数
            int followerCount = followMapper.selectCount(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, userInfo.getId()));
            userProfileVO.setFansCount(followerCount);

            return Result.success(userProfileVO);
        } else {
            return Result.fail("您尚未登录");
        }



    }

    //根据用户id获取用户信息，用于进入他人的个人主页
//    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/getUserProfileVOById")
    public Result getUserProfileVOById (@RequestBody Long id) {
        System.out.println("用户id"+id);
        UserProfileVO userProfile = userInfoService.getUserProfileById(id);
        return Result.success(userProfile);
    }

    @PreAuthorize("hasAuthority('user:update')")
    @GetMapping("/updateUserNickname")
    public Result updateUserNickname(String data,Principal principal) {

            UserInfo existence = userInfoService.getByNickname(data);
            System.out.println("ex"+existence);
            if (ObjectUtils.isNull(existence)){
                UserInfo userInfo = userInfoService.getByUsername(principal.getName());
                userInfo.setNickname(data);
                userInfoService.updateById(userInfo);
                return Result.success(userInfoService.getByUsername(principal.getName()).getNickname());
            } else {
                return Result.fail("存在相同的昵称");
            }


        }

    @PreAuthorize("hasAuthority('user:update')")
    @PostMapping("/updateUserIntroduction")
    public Result updateUserIntroduction(@RequestBody String data,Principal principal) {

        System.out.println(data);
        UserInfo userInfo = userInfoService.getByUsername(principal.getName());
        userInfo.setBriefintroduction(data);
        userInfoService.updateById(userInfo);


        return Result.success(userInfoService.getByUsername(principal.getName()).getBriefintroduction());
    }

    //获取随机用户，通过积分排序，用于用户推荐
    @GetMapping("/getRandomUsers")
    public Result getRandomUsers(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                 @RequestParam(value = "size", defaultValue = "5") Integer size){
        Page<UserInfo> userInfoPage = userInfoMapper.selectUserOrderByScore(new Page<>(current, size));
        return Result.success(userInfoPage);
    }

}
