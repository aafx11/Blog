package com.djh.service;

import com.djh.entity.ArticleTag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djh.entity.Tag;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
public interface ArticleTagService extends IService<ArticleTag> {
    List<ArticleTag> getTagByArticleId(Long articleId);

    void createArticleTags(Long articleId,List<Tag> tags);
}
