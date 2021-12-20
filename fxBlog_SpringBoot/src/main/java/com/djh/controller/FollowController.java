package com.djh.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.lang.Result;
import com.djh.common.vo.UserProfileVO;
import com.djh.entity.Follow;
import com.djh.entity.UserInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author djh
 * @PathVariable获取浏览器里的占位符
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/follow")
public class FollowController extends BaseController {


    //检查是否关注
    @GetMapping("/hasFollow/{articleUserId}")
    public Result hasFollow(@PathVariable("articleUserId") Long articleUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> map = new HashMap<>();
        map.put("isFollow", false);
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo = userInfoService.getByUsername(auth.getName());
            if (!ObjectUtils.isEmpty(userInfo)) {
                Follow hasFollow = followService.getOne(new LambdaQueryWrapper<Follow>()
                        .eq(Follow::getFollowerId, articleUserId)//被关注人
                        .eq(Follow::getUserId, userInfo.getId()));//关注人
                if (!ObjectUtils.isEmpty(hasFollow)) {
                    map.put("isFollow", true);
                }
            }
        }
        return Result.success(map);
    }

    //关注
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/followOneUser/{followerId}")
    public Result followOneUser(Authentication authentication, @PathVariable("followerId") Long followerId) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo = userInfoService.getByUsername(authentication.getName());
            if (followerId.equals(userInfo.getId())) {
                return Result.fail("自己不能关注自己喔");
            }

            Follow hasFollow = followService.getOne(new LambdaQueryWrapper<Follow>()
                    .eq(Follow::getFollowerId, followerId)//被关注人
                    .eq(Follow::getUserId, userInfo.getId()));//关注人
            if (!ObjectUtils.isEmpty(hasFollow)) {
                Result.fail("已关注");
            }

            Follow follow = new Follow();
            follow.setUserId(userInfo.getId());
            follow.setFollowerId(followerId);
            followService.save(follow);
            return Result.success("关注成功", null);
        } else {
            return Result.success("请先登录");
        }

    }

    //取消关注
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/unFollowOneUser/{articleUserId}")
    public Result unFollowOneUser(Principal principal, @PathVariable("articleUserId") Long articleUserId) {
        UserInfo userInfo = userInfoService.getByUsername(principal.getName());

        Follow hasFollow = followService.getOne(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getFollowerId, articleUserId)//被关注人
                .eq(Follow::getUserId, userInfo.getId()));//关注人
        if (ObjectUtils.isEmpty(hasFollow)) {
            Result.fail("未关注");
        }

        followService.remove(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getFollowerId, articleUserId)
                .eq(Follow::getUserId, userInfo.getId()));

        return Result.success("成功取消关注", null);
    }

    //获取本用户关注列表（分页）
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/getFollowListByPage")
    public Result getFollowListByPage(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo = userInfoService.getByUsername(auth.getName());
            List<Follow> followList = followService.list(new QueryWrapper<Follow>().eq("user_id", userInfo.getId()));


            Page<Long> userIdList = followMapper.getFollowList(new Page<>(current, size), userInfo.getId());
            System.out.println("获取的用户id" + userIdList.getRecords());

            List<UserProfileVO> profileVOList = new ArrayList<>();
            userIdList.getRecords().forEach(id -> {
                UserProfileVO userProfileVO = userInfoService.getUserProfileById(id);
                profileVOList.add(userProfileVO);
            });
            Map<String, Object> result = new HashMap<>();
            result.put("profileVOList", profileVOList);
            result.put("total", followList.size());
            return Result.success(result);
        } else {
            return Result.fail("您尚未登录");
        }

    }

    //通过用户id获取关注列表（分页）
    @GetMapping("/getFollowListByUserId")
    public Result getFollowListByUserId(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                        @RequestParam(value = "userId") Long userId) {

        List<Follow> followList = followService.list(new QueryWrapper<Follow>().eq("user_id", userId));


        Page<Long> userIdList = followMapper.getFollowList(new Page<>(current, size), userId);
        System.out.println("获取的用户id" + userIdList.getRecords());

        List<UserProfileVO> profileVOList = new ArrayList<>();
        userIdList.getRecords().forEach(id -> {
            UserProfileVO userProfileVO = userInfoService.getUserProfileById(id);
            profileVOList.add(userProfileVO);
        });
        Map<String, Object> result = new HashMap<>();
        result.put("profileVOList", profileVOList);
        result.put("total", followList.size());
        return Result.success(result);


    }

    //获取完整的关注列表(非分页)
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/getFollowList")
    public Result getFollowList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo = userInfoService.getByUsername(auth.getName());
            List<Follow> followList = followService.list(new QueryWrapper<Follow>().eq("user_id", userInfo.getId()));
            List<Long> idList = new ArrayList<>();
            followList.forEach(follow -> {
                idList.add(follow.getFollowerId());
            });
            List<UserProfileVO> profileVOList = new ArrayList<>();

            idList.forEach(id -> {
                UserProfileVO userProfileVO = userInfoService.getUserProfileById(id);
                profileVOList.add(userProfileVO);
            });

            return Result.success(profileVOList);


        } else {
            return Result.fail("您尚未登录");
        }
    }

    //获取粉丝列表(分页)
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/getFansList")
    public Result getFansList(Principal principal, @RequestParam(value = "current", defaultValue = "1") Integer current,
                              @RequestParam(value = "pageSize", defaultValue = "5") Integer size) {
        UserInfo userInfo = userInfoService.getByUsername(principal.getName());
        Page<Long> fanIds = followMapper.getFanList(new Page<>(current, size), userInfo.getId());

        System.out.println("粉丝id" + fanIds.getRecords());

        List<UserProfileVO> profileVOList = new ArrayList<>();
        fanIds.getRecords().forEach(id -> {
            UserProfileVO userProfileVO = userInfoService.getUserProfileById(id);
            profileVOList.add(userProfileVO);
        });

        return Result.success(profileVOList);
    }

    //通过用户id获取粉丝列表(分页)
    @GetMapping("/getFansListByUserId")
    public Result getFansListByUserId(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer size,
                                      @RequestParam(value = "userId") Long userId) {
        Page<Long> fanIds = followMapper.getFanList(new Page<>(current, size), userId);


        List<UserProfileVO> profileVOList = new ArrayList<>();
        fanIds.getRecords().forEach(id -> {
            UserProfileVO userProfileVO = userInfoService.getUserProfileById(id);
            profileVOList.add(userProfileVO);
        });

        return Result.success(profileVOList);
    }


}
