package com.djh.service;

import com.djh.entity.VideoCollect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
public interface VideoCollectService extends IService<VideoCollect> {
    //判断是否收藏视频
    Boolean hasCollect(Long videoId);
    //收藏功能
    boolean setCollect(Long videoId);
    //取消收藏
    boolean removeCollect(Long videoId);

    //计算视频收藏数量
    int countCollect(Long videoId);
}

