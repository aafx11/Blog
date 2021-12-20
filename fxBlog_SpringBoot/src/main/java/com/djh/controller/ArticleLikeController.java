package com.djh.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.djh.common.lang.Result;
import com.djh.entity.ArticleLike;
import com.djh.entity.Follow;
import com.djh.entity.UserInfo;
import com.djh.service.UserInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-09-08
 */
@RestController
@RequestMapping("/articleLike")
public class ArticleLikeController extends BaseController {

    //点赞功能
//    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/likewise")
    public Result likewise(@RequestBody Long articleId){
        System.out.println("文章id"+articleId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo =userInfoService.getByUsername(auth.getName());
            ArticleLike articleLike = new ArticleLike();
            articleLike.setUserId(userInfo.getId());
            articleLike.setArticleId(articleId);
            articleLikeService.save(articleLike);
            return Result.success("点赞成功");
        } else {
            return Result.fail("请先登录");
        }

    }

    //取消点赞
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/unLikewise")
    public Result unLikewise(@RequestBody Long articleId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo =userInfoService.getByUsername(auth.getName());

            articleLikeService.remove(new LambdaQueryWrapper<ArticleLike>()
                                        .eq(ArticleLike::getUserId,userInfo.getId())
                                        .eq(ArticleLike::getArticleId,articleId));
            return Result.success(null);
        } else {
           return Result.fail("请先登录");
        }
    }
}
