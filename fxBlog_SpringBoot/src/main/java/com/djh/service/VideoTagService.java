package com.djh.service;

import com.djh.entity.TagsVideo;
import com.djh.entity.VideoTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
public interface VideoTagService extends IService<VideoTag> {

    //创建视频与标签的关联
    void createVideoTags(Long videoId, List<TagsVideo> tags);

    //通过视频id找到对应的标签
    List<VideoTag> findTagsByVideoId(Long videoId);

    List<TagsVideo> findVideoTagsList(Long videoId);
}
