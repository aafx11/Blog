package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.djh.entity.ArticleCollect;
import com.djh.entity.UserInfo;
import com.djh.entity.VideoCollect;
import com.djh.entity.VideoLike;
import com.djh.mapper.VideoCollectMapper;
import com.djh.service.UserInfoService;
import com.djh.service.VideoCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
@Service
public class VideoCollectServiceImpl extends ServiceImpl<VideoCollectMapper, VideoCollect> implements VideoCollectService {

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    VideoCollectService videoCollectService;

    //判断视频是否已收藏
    public Boolean hasCollect(Long videoId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo = userInfoService.getByUsername(auth.getName());
            if (!ObjectUtils.isEmpty(userInfo)){
                VideoCollect videoCollect = videoCollectService.getOne(new LambdaQueryWrapper<VideoCollect>()
                          .eq(VideoCollect::getVideoId,videoId)
                          .eq(VideoCollect::getUserId,userInfo.getId()));
                if (!ObjectUtils.isEmpty(videoCollect)){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //收藏功能
    public boolean setCollect(Long videoId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = userInfoService.getByUsername(auth.getName());
        VideoCollect videoCollect = new VideoCollect();
        videoCollect.setUserId(userInfo.getId());
        videoCollect.setVideoId(videoId);
        boolean save = videoCollectService.save(videoCollect);
        return save;
    }

    //取消收藏
    public boolean removeCollect(Long videoId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = userInfoService.getByUsername(auth.getName());

        boolean remove = videoCollectService.remove(new LambdaQueryWrapper<VideoCollect>()
                .eq(VideoCollect::getUserId, userInfo.getId())
                .eq(VideoCollect::getVideoId, videoId));
        return remove;
    }

    //计算收藏数量,通过视频id
    public int countCollect(Long videoId){
        int count = videoCollectService.count(new QueryWrapper<VideoCollect>().eq("video_id", videoId));
        return count;
    }
}
