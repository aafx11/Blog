package com.djh.service;

import com.djh.entity.VideoLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
public interface VideoLikeService extends IService<VideoLike> {
    //判断文章是否点赞
    Boolean hasLike(Long videoId);

    //点赞功能
    boolean setVideoLike(Long videoId);

    //取消点赞
    boolean removeLike(Long videoId);

    //计算视频点赞数量
    int countLike(Long videoId);
}
