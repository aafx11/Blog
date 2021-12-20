package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.djh.entity.ArticleCollect;
import com.djh.entity.ArticleLike;
import com.djh.entity.UserInfo;
import com.djh.mapper.ArticleCollectMapper;
import com.djh.service.ArticleCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djh.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-09-09
 */
@Service
public class ArticleCollectServiceImpl extends ServiceImpl<ArticleCollectMapper, ArticleCollect> implements ArticleCollectService {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    ArticleCollectService articleCollectService;

    //检查文章是否已收藏
    public Boolean hasCollect(Authentication authentication, Long articleId) {
        UserInfo userInfo = userInfoService.getByUsername(authentication.getName());
        if (!ObjectUtils.isEmpty(userInfo)) {
            ArticleCollect articleCollect = articleCollectService.getOne(new LambdaQueryWrapper<ArticleCollect>()
                    .eq(ArticleCollect::getArticleId, articleId)
                    .eq(ArticleCollect::getUserId, userInfo.getId()));

            if (!ObjectUtils.isEmpty(articleCollect)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //收藏文章
    public Boolean collect(Long userId, Long articleId) {
        ArticleCollect articleCollect = new ArticleCollect();
        articleCollect.setArticleId(articleId);
        articleCollect.setUserId(userId);
        return articleCollectService.save(articleCollect);

    }

    //取消收藏
    public Boolean unCollect(Long userId, Long articleId) {
       Boolean aBoolean=articleCollectService.remove(new LambdaQueryWrapper<ArticleCollect>()
                .eq(ArticleCollect::getUserId,userId)
                .eq(ArticleCollect::getArticleId,articleId));

       return aBoolean;
    }

}
