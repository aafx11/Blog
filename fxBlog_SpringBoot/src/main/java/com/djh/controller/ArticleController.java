package com.djh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.DTO.ArticleDto;
import com.djh.common.lang.Result;
import com.djh.common.vo.articleVO;
import com.djh.entity.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @GetMapping("/articleList")
    public Result articleList(
                              @RequestParam(value = "tab", defaultValue = "hot") String tab,
                              @RequestParam(value = "current", defaultValue = "1") Integer current,
                              @RequestParam(value = "size", defaultValue = "10") Integer size) {

        System.out.println("模式" + tab);
        System.out.println("当前页" + current);
        System.out.println("一页几条信息" + size);
        Page<articleVO> list = articleService.getList(new Page<>(current, size), tab);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //判断是否点赞
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(articleService.hasLike(auth,articleVO.getId()));
                articleVO.setHasCollect(articleCollectService.hasCollect(auth,articleVO.getId()));

            });
            System.out.println("登录");
        }else {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(false);
                articleVO.setHasCollect(false);
            });
            System.out.println("没登陆");
        }

        //统计文章的点赞和收藏数量
        list.getRecords().forEach(articleVO -> {
            int likeCount = articleLikeService.count(new QueryWrapper<ArticleLike>().eq("article_id",articleVO.getId()));
            articleVO.setLikeCount(likeCount);
            int collectCount = articleCollectService.count(new QueryWrapper<ArticleCollect>().eq("article_id",articleVO.getId()));
            articleVO.setCollects(collectCount);
        });

        return Result.success(list);
    }

    @GetMapping("/ArticleListByLikeSearch")
    public Result ArticleListByLikeSearch(   @RequestParam(value = "searchContent") String searchContent,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size){



            Page<articleVO> list = articleService.getArticleListByLikeSearch(new Page<>(current,size),searchContent);
            return Result.success(list);


    }

    @GetMapping("/articleListByTag")
    public Result articleListByTag(Long tagId,

                                   @RequestParam(value = "current", defaultValue = "1") Integer current,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size){
        System.out.println("标签id"+tagId);

        Page<articleVO> list = articleService.getListByTag(new Page<>(current, size),tagId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //判断是否点赞和收藏
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(articleService.hasLike(auth,articleVO.getId()));
                articleVO.setHasCollect(articleCollectService.hasCollect(auth,articleVO.getId()));
            });
            System.out.println("登录");
        }else {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(false);
                articleVO.setHasCollect(false);
            });
            System.out.println("没登陆");
        }

        //统计文章的点赞和收藏数量
        list.getRecords().forEach(articleVO -> {
            int likeCount = articleLikeService.count(new QueryWrapper<ArticleLike>().eq("article_id",articleVO.getId()));
            articleVO.setLikeCount(likeCount);
            int collectCount = articleCollectService.count(new QueryWrapper<ArticleCollect>().eq("article_id",articleVO.getId()));
            articleVO.setCollects(collectCount);
        });


        return Result.success(list);
    }

    @GetMapping("/getFollowArticle")
    public Result getFollowArticle(
                            Principal principal,
                            Authentication authentication,
                            @RequestParam(value = "current", defaultValue = "1") Integer current,
                            @RequestParam(value = "size", defaultValue = "10") Integer size){
        System.out.println("当前页" + current);
        System.out.println("一页几条信息" + size);
        UserInfo userInfo = userInfoService.getByUsername(principal.getName());

        Page<articleVO> list = articleService.getListByFollow(new Page<>(current, size),userInfo.getId());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //判断是否点赞
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(articleService.hasLike(auth,articleVO.getId()));
                articleVO.setHasCollect(articleCollectService.hasCollect(auth,articleVO.getId()));
            });
            System.out.println("登录");
        }else {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(false);
                articleVO.setHasCollect(false);
            });
            System.out.println("没登陆");
        }

        //统计文章的点赞和收藏数量
        list.getRecords().forEach(articleVO -> {
            int count = articleLikeService.count(new QueryWrapper<ArticleLike>().eq("article_id",articleVO.getId()));
            articleVO.setLikeCount(count);
            int collectCount = articleCollectService.count(new QueryWrapper<ArticleCollect>().eq("article_id",articleVO.getId()));
            articleVO.setCollects(collectCount);
        });

        return Result.success(list);
    }

    //获取个人文章
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/getPersonalArticle")
    public Result getPersonalArticle(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer size){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserInfo userInfo  = userInfoService.getByUsername(auth.getName());
        System.out.println("获取的用户id"+userInfo.getId());
        Long userId = userInfo.getId();
        Page<articleVO> list = articleService.getListByUserId(new Page<>(current, size),userId);


        //判断是否点赞
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(articleService.hasLike(auth,articleVO.getId()));
                articleVO.setHasCollect(articleCollectService.hasCollect(auth,articleVO.getId()));

            });
            System.out.println("登录");
        }else {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(false);
                articleVO.setHasCollect(false);
            });
            System.out.println("没登陆");
        }

        //统计文章的点赞和收藏数量
        list.getRecords().forEach(articleVO -> {
            int count = articleLikeService.count(new QueryWrapper<ArticleLike>().eq("article_id",articleVO.getId()));
            articleVO.setLikeCount(count);
            int collectCount = articleCollectService.count(new QueryWrapper<ArticleCollect>().eq("article_id",articleVO.getId()));
            articleVO.setCollects(collectCount);
        });
        return Result.success(list);
    }

    //获取个人文章列表，模糊查询
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/getListByIdAndLike")
    public Result getListByIdAndLike(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size,
                                     @RequestParam(value = "searchContent",defaultValue = "") String searchContent){
        System.out.println("纸张大小"+size);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserInfo userInfo  = userInfoService.getByUsername(auth.getName());
        Long userId = userInfo.getId();
        Page<articleVO> list = articleService.getListByIdAndLike(new Page<>(current, size),userId,searchContent);


        //判断是否点赞
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(articleService.hasLike(auth,articleVO.getId()));
                articleVO.setHasCollect(articleCollectService.hasCollect(auth,articleVO.getId()));

            });
            System.out.println("登录");
        }else {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(false);
                articleVO.setHasCollect(false);
            });
            System.out.println("没登陆");
        }

        //统计文章的点赞和收藏数量
        list.getRecords().forEach(articleVO -> {
            int count = articleLikeService.count(new QueryWrapper<ArticleLike>().eq("article_id",articleVO.getId()));
            articleVO.setLikeCount(count);
            int collectCount = articleCollectService.count(new QueryWrapper<ArticleCollect>().eq("article_id",articleVO.getId()));
            articleVO.setCollects(collectCount);
        });
        return Result.success(list);
    }

    //用于发布文章
    @PostMapping("/editArticle")
    public Result editArticle(@RequestBody ArticleDto articleDto, Principal principal) {
        UserInfo userInfo = userInfoService.getByUsername(principal.getName());
        Article article = articleService.edit(articleDto, userInfo);

        return Result.success(article);
    }

    //获取文章详情以及作者的信息
    @GetMapping("/getArticleById")
    public Result getArticleById(Long id) {
        System.out.println("获取id" + id);

        Map<String, Object> articleDetail = articleService.getArticleDetailsById(id);

        return Result.success(articleDetail);
    }

    //根据文章id删除文章
    @PreAuthorize("hasAuthority('article:delete')")
    @Transactional
    @PostMapping("/deleteArticleByIds")
    public Result deleteArticleByIds(@RequestBody Long[] articleIds){

        articleService.removeByIds(Arrays.asList(articleIds));

        for(int i = 0;i<articleIds.length;i++){
            articleTagService.remove(new QueryWrapper<ArticleTag>().in("article_id",articleIds[i]));
            articleCollectService.remove(new QueryWrapper<ArticleCollect>().in("article_id",articleIds[i]));
            articleLikeService.remove(new QueryWrapper<ArticleLike>().in("article_id",articleIds[i]));
        }


        return Result.success("删除成功");
    }

    //根据用户id获取文章列表
    @GetMapping("/getArticleByUserId")
    public Result getArticleByUserId(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size,
                                     @RequestParam(value = "searchContent",defaultValue = "")String searchContent,
                                     @RequestParam(value = "userId")Long userId){

        Page<articleVO> list = articleService.getListByIdAndLike(new Page<>(current, size), userId, searchContent);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //判断是否点赞
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(articleService.hasLike(auth,articleVO.getId()));
                articleVO.setHasCollect(articleCollectService.hasCollect(auth,articleVO.getId()));

            });
            System.out.println("登录");
        }else {
            list.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(false);
                articleVO.setHasCollect(false);
            });
            System.out.println("没登陆");
        }
        list.getRecords().forEach(articleVO -> {
            int count = articleLikeService.count(new QueryWrapper<ArticleLike>().eq("article_id",articleVO.getId()));
            articleVO.setLikeCount(count);
            int collectCount = articleCollectService.count(new QueryWrapper<ArticleCollect>().eq("article_id",articleVO.getId()));
            articleVO.setCollects(collectCount);
        });
        return Result.success(list);
    }
}
