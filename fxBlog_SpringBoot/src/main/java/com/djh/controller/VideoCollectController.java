package com.djh.controller;


import com.djh.common.lang.Result;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
@RestController
@RequestMapping("/videoCollect")
public class VideoCollectController extends BaseController {

    //收藏功能
    @PostMapping("/collect")
    public Result collect(@RequestBody Long videoId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            boolean b = videoCollectService.setCollect(videoId);
            if (b){
                return Result.success("收藏成功");
            } else {
                return Result.fail("收藏失败");
            }
        }else {
            return Result.fail("请先登录");

        }
    }


    //取消收藏
    @PostMapping("/unCollect")
    public Result unCollect(@RequestBody Long videoId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            boolean b = videoCollectService.removeCollect(videoId);
            if (b){
                return Result.success("取消收藏");
            } else {
                return Result.fail("取消失败");
            }
        }else {
            return Result.fail("请先登录");

        }
    }
}
