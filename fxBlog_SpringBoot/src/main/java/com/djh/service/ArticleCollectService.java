package com.djh.service;

import com.djh.entity.ArticleCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.Authentication;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-09-09
 */
public interface ArticleCollectService extends IService<ArticleCollect> {
    Boolean hasCollect(Authentication authentication, Long articleId);
    Boolean collect(Long userId,Long articleId);
    Boolean unCollect(Long userId, Long articleId);
}
