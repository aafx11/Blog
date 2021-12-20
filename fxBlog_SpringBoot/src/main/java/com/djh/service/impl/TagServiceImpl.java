package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.entity.Tag;
import com.djh.mapper.TagMapper;
import com.djh.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public List<Tag> insertTags(List<String> tags) {

        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tags) {
            Tag tag = this.baseMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName,tagName));
            if(tag == null) {
                tag = Tag.builder().name(tagName).build();
                this.baseMapper.insert(tag);
            } else {
                tag.setArticleCount(tag.getArticleCount() + 1);
                this.baseMapper.updateById(tag);
            }
            tagList.add(tag);
        }

        return tagList;
    }

    public Page<Tag>  getTagListByPage(Page<Tag> page){
        Page<Tag> tagList = this.baseMapper.tagListByPage(page);
        return tagList;
    }
}
