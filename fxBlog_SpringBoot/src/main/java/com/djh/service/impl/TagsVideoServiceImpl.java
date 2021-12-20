package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.entity.ArticleTag;
import com.djh.entity.Tag;
import com.djh.entity.TagsVideo;
import com.djh.entity.VideoTag;
import com.djh.mapper.TagsVideoMapper;
import com.djh.service.TagsVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
@Service
public class TagsVideoServiceImpl extends ServiceImpl<TagsVideoMapper, TagsVideo> implements TagsVideoService {

    @Autowired
    TagsVideoService tagsVideoService;



    //创建新的视频标签
    @Override
    public List<TagsVideo> createNewVideoTags(List<String> tags){
        List<TagsVideo> tagList = new ArrayList<>();

        for (String tagName : tags){
            TagsVideo tagsVideo = this.baseMapper.selectOne(new LambdaQueryWrapper<TagsVideo>().eq(TagsVideo::getName,tagName));
            System.out.println("视频标签"+tagsVideo);
            if (tagsVideo == null){
                //如果实体类为null，无法直接用set方法插入属性值
                tagsVideo = tagsVideo.builder().name(tagName).videoCount(1).build();
                this.baseMapper.insert(tagsVideo);
            } else {
                tagsVideo.setVideoCount(tagsVideo.getVideoCount()+1);
                this.baseMapper.updateById(tagsVideo);
            }
            tagList.add(tagsVideo);
        }
        return tagList;
    }


    //获取所有视频标签
    public Page<TagsVideo> findVideoTags(Page<TagsVideo> page){
        Page<TagsVideo> tagsVideoPage = this.baseMapper.selectVideoTags(page);
        return tagsVideoPage;
    }
}
