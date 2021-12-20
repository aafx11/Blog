package com.djh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.entity.TagsVideo;
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
public interface TagsVideoService extends IService<TagsVideo> {
    //创建新的视频标签
    List<TagsVideo> createNewVideoTags(List<String> tags);
    //获取所有视频标签
    Page<TagsVideo> findVideoTags(Page<TagsVideo> page);
}
