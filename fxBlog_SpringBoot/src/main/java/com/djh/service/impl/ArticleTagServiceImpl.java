package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.djh.entity.ArticleTag;
import com.djh.entity.Tag;
import com.djh.mapper.ArticleTagMapper;
import com.djh.service.ArticleTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djh.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Autowired
    ArticleTagService articleTagService;

    @Autowired
    TagService tagService;
    @Override
    public List<ArticleTag> getTagByArticleId(Long articleId) {

        QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArticleTag::getArticleId,articleId);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void createArticleTags(Long articleId, List<Tag> tags) {
        //删除该文章对应的tags
        this.baseMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId,articleId));

        //重新保存新的tags
        tags.forEach(tag -> {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(articleId);
            articleTag.setTagId(tag.getId());
            articleTagService.save(articleTag);
        });
    }
}
