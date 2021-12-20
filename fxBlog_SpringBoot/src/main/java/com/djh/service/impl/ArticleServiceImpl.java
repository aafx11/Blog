package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.DTO.ArticleDto;
import com.djh.common.lang.Result;
import com.djh.common.vo.UserProfileVO;
import com.djh.common.vo.articleVO;
import com.djh.entity.*;
import com.djh.mapper.ArticleMapper;
import com.djh.mapper.TagMapper;
import com.djh.mapper.UserInfoMapper;
import com.djh.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    ArticleTagService articleTagService;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    ArticleService articleService;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    TagService tagService;

    @Autowired
    FollowService followService;

    @Autowired
    ArticleLikeService articleLikeService;

    @Autowired
    ArticleCollectService articleCollectService;

    //获取7天热门文章或最新文章
    @Override
    public Page<articleVO> getList(Page<articleVO> page, String tab) {

        Page<articleVO> articleList = this.baseMapper.articleListByPage(page, tab);
        setArticleTags(articleList);

        return articleList;
    }

    @Override
    public Page<articleVO> getArticleListByLikeSearch(Page<articleVO> page, String searchContent) {
        //模糊搜索文章
        Page<articleVO> articleList = this.baseMapper.articleListByLikeSearch(page, searchContent);

        setArticleTags(articleList);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        if (!(auth instanceof AnonymousAuthenticationToken)) {
            articleList.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(articleService.hasLike(auth, articleVO.getId()));
                articleVO.setHasCollect(articleCollectService.hasCollect(auth, articleVO.getId()));
            });
        } else {
            articleList.getRecords().forEach(articleVO -> {
                articleVO.setHasLike(false);
                articleVO.setHasCollect(false);
            });
        }


        //统计文章的点赞和收藏数量
        articleList.getRecords().forEach(articleVO -> {
            int likeCount = articleLikeService.count(new QueryWrapper<ArticleLike>().eq("article_id", articleVO.getId()));
            articleVO.setLikeCount(likeCount);
            int collectCount = articleCollectService.count(new QueryWrapper<ArticleCollect>().eq("article_id", articleVO.getId()));
            articleVO.setCollects(collectCount);
        });


        return articleList;
    }

    @Override
    public Page<articleVO> getListByTag(Page<articleVO> page, Long tagId) {
        System.out.println("获取的标签id" + tagId);
        Page<articleVO> articleList = this.baseMapper.articleListByTag(page, tagId);
        setArticleTags(articleList);

        return articleList;
    }

    @Override
    public Page<articleVO> getListByFollow(Page<articleVO> page, Long userId) {

        List<Follow> follows = followService.list(new QueryWrapper<Follow>().eq("user_id", userId));
        System.out.println("关注列表" + follows);
        System.out.println("关注列表" + ObjectUtils.isEmpty(follows));

//        Assert.notEmpty(follows,"尚未关注任何人");
        Assert.notEmpty(follows, "尚未关注任何人");//不是空的返回false

        Page<articleVO> articleList = this.baseMapper.articleListByFollow(page, userId);
        setArticleTags(articleList);
        return articleList;
    }

    //获取个人文章列表
    public Page<articleVO> getListByUserId(Page<articleVO> page, Long userId) {
        Page<articleVO> articleList = this.baseMapper.articleListByUserId(page, userId);
        setArticleTags(articleList);
        return articleList;
    }

    //获取个人文章列表，模糊查询
    public Page<articleVO> getListByIdAndLike(Page<articleVO> page, Long userId,String searchContent) {
        Page<articleVO> articleVOPage = this.baseMapper.articleListByIdAndLike(page,userId,searchContent);
        setArticleTags(articleVOPage);

        return articleVOPage;
    }


    //给文章找到自己的标签
    private void setArticleTags(Page<articleVO> articleList) {
        articleList.getRecords().forEach(article -> {
            List<ArticleTag> articleTags = articleTagService.getTagByArticleId(article.getId());
            if (!articleTags.isEmpty()) {
                List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
                List<Tag> tags = tagMapper.selectBatchIds(tagIds);
                article.setTags(tags);
            }
        });
    }

    //发布文章
    @Override
    public Article edit(ArticleDto articleDto, UserInfo userInfo) {
        Article articleIsNull = articleService.getOne(new QueryWrapper<Article>().eq("title", articleDto.getTitle()));
        Assert.isNull(articleIsNull, "标题已存在，请重新输入");//如果传递的参数不为null就报错

        //将文章保存到数据库
        Article article = Article.builder()
                .userId(userInfo.getId())
                .title(articleDto.getTitle())
                .introduction(articleDto.getIntroduction())
                .content(articleDto.getContent())
                .cover(articleDto.getCover())
                .build();
        article.setCreated(LocalDateTime.now());
        articleService.save(article);

        //发布文章后，用户积分增加1
        int score = userInfo.getScore() + 1;
        userInfo.setScore(score);
        userInfoService.updateById(userInfo);

        //保存标签
        if (!ObjectUtils.isEmpty(articleDto.getTags())) {
            List<Tag> tags = tagService.insertTags(articleDto.getTags());
            articleTagService.createArticleTags(article.getId(), tags);
        }


        return article;
    }

    //获取文章详情以及作者的信息
    @Override
    public Map<String, Object> getArticleDetailsById(Long articleId) {
        Map<String, Object> articleDetail = new HashMap<>();
        System.out.println("获取的id" + articleId);
        //文章详细内容
        Article article = articleService.getById(articleId);
        System.out.println("搜索的文章" + article);

        Assert.notNull(article, "当前文章已被作者删除");

        //文章观看人数加一
        article.setView(article.getView() + 1);
        articleService.updateById(article);

        //判断是否点赞了该文章
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            article.setHasLike(articleService.hasLike(auth, articleId));
            article.setHasCollect(articleCollectService.hasCollect(auth, articleId));
        } else {
            article.setHasLike(false);
            article.setHasCollect(false);
        }

        //统计文章点赞和收藏数量
        int count = articleLikeService.count(new QueryWrapper<ArticleLike>().eq("article_id", articleId));
        article.setLikeCount(count);
        int collectCount = articleCollectService.count(new QueryWrapper<ArticleCollect>().eq("article_id", articleId));
        article.setCollects(collectCount);

        //文章
        articleDetail.put("article", article);

        //文章的标签
        QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArticleTag::getArticleId, article.getId());
        Set<String> set = new HashSet<>();
        for (ArticleTag articleTag : articleTagService.list(wrapper)) {
            set.add(Long.toString(articleTag.getTagId()));
        }
        System.out.println(set);

        if (!ObjectUtils.isEmpty(set)) {
            List<Tag> tagList = tagService.listByIds(set);
            articleDetail.put("tags", tagList);
        }

        //获取文章的作者信息
        UserProfileVO userProfileVO = userInfoService.getUserProfileById(article.getUserId());
        articleDetail.put("userProfile", userProfileVO);


        return articleDetail;
    }

    //检查文章是否已点赞
    public Boolean hasLike(Authentication authentication, Long articleId) {
        UserInfo userInfo = userInfoService.getByUsername(authentication.getName());
        Boolean isLike = false;
        if (!ObjectUtils.isEmpty(userInfo)) {
            ArticleLike articleLike = articleLikeService.getOne(new LambdaQueryWrapper<ArticleLike>()
                    .eq(ArticleLike::getArticleId, articleId)
                    .eq(ArticleLike::getUserId, userInfo.getId()));
            if (!ObjectUtils.isEmpty(articleLike)) {
                isLike = true;
            } else {
                return false;
            }
        }

        return isLike;
    }

//    public Boolean delete()


}
