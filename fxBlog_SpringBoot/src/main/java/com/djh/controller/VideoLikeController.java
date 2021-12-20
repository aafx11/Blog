package com.djh.controller;


import com.baomidou.mybatisplus.extension.api.R;
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
@RequestMapping("/videoLike")
public class VideoLikeController extends BaseController {

    //点赞功能
    @PostMapping("/likewise")
    public Result likewise(@RequestBody Long videoId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            boolean b = videoLikeService.setVideoLike(videoId);
            if (b){
                return Result.success("点赞成功");
            } else {
                return Result.fail("点赞失败");
            }

        } else {
            return Result.fail("请先登录");
        }
    }

    @PostMapping("/unLikewise")
    public Result unLikewise(@RequestBody Long videoId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            boolean b = videoLikeService.removeLike(videoId);
            if (b){
                return Result.success("取消点赞");
            } else {
                return Result.fail("取消失败");
            }
        }else {
            return Result.fail("请先登录");
        }
    }
}
