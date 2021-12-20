package com.djh.controller;


import com.djh.common.lang.Result;
import com.djh.entity.UserInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-09-09
 */
@RestController
@RequestMapping("/articleCollect")
public class ArticleCollectController extends BaseController {

    //收藏
//    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/collect")
    public Result collect(@RequestBody Long articleId) {
        System.out.println("收藏文章id"+articleId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo = userInfoService.getByUsername(auth.getName());
            if (articleCollectService.collect(userInfo.getId(), articleId)) {
                return Result.success("收藏成功");
            } else {
                return Result.fail("收藏失败");
            }
        } else {
            return Result.fail("请先登录");
        }
    }

    //取消收藏
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/unCollect")
    public Result unCollect(@RequestBody Long articleId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo = userInfoService.getByUsername(auth.getName());
            if (articleCollectService.unCollect(userInfo.getId(), articleId)) {
                return Result.success("取消收藏");
            }else {
                return Result.fail("取消失败");
            }
        } else {
            return Result.fail("请先登录");
        }
    }
}
