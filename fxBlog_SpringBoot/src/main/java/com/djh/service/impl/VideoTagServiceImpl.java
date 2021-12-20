package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.djh.entity.ArticleTag;
import com.djh.entity.TagsVideo;
import com.djh.entity.VideoTag;
import com.djh.mapper.VideoTagMapper;
import com.djh.service.TagsVideoService;
import com.djh.service.VideoTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
@Service
public class VideoTagServiceImpl extends ServiceImpl<VideoTagMapper, VideoTag> implements VideoTagService {

    @Autowired
    VideoTagService videoTagService;

    @Autowired
    TagsVideoService tagsVideoService;

    //通过视频id找到对应的标签
    public List<VideoTag> findTagsByVideoId(Long videoId){
        QueryWrapper<VideoTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(VideoTag::getVideoId,videoId);

        return this.baseMapper.selectList(wrapper);
    }

    //建立视频与标签的中间表
    @Override
    public void createVideoTags(Long videoId, List<TagsVideo> tags){
        this.baseMapper.delete(new LambdaQueryWrapper<VideoTag>().eq(VideoTag::getVideoId,videoId));

        tags.forEach(tag -> {
            VideoTag videoTag = new VideoTag();
            videoTag.setVideoId(videoId);
            videoTag.setTagId(tag.getId());
            videoTagService.save(videoTag);
        });
    }

    //视频标签
    public List<TagsVideo> findVideoTagsList(Long videoId){
        QueryWrapper<VideoTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(VideoTag::getVideoId,videoId);
        Set<String> set = new HashSet<>();
        List<TagsVideo> tagList = new ArrayList<>();
        for (VideoTag videoTag : videoTagService.list(wrapper)){
            set.add(Long.toString(videoTag.getTagId()));
        }
        if (!ObjectUtils.isEmpty(set)){
            tagList = tagsVideoService.listByIds(set);

            return tagList;
        }

        return tagList;
    }

}
