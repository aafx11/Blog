package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.djh.entity.ArticleLike;
import com.djh.entity.UserInfo;
import com.djh.entity.VideoLike;
import com.djh.mapper.VideoLikeMapper;
import com.djh.service.UserInfoService;
import com.djh.service.VideoLikeService;
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
public class VideoLikeServiceImpl extends ServiceImpl<VideoLikeMapper, VideoLike> implements VideoLikeService {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    VideoLikeService videoLikeService;

    //判断视频是否点赞
    public Boolean hasLike(Long videoId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo = userInfoService.getByUsername(auth.getName());
            Boolean isLike = false;
            if (!ObjectUtils.isEmpty(userInfo)){
                VideoLike videoLike = videoLikeService.getOne(new LambdaQueryWrapper<VideoLike>()
                        .eq(VideoLike::getVideoId,videoId)
                        .eq(VideoLike::getUserId,userInfo.getId()));
                if (!ObjectUtils.isEmpty(videoLike)){
                    isLike = true;
                } else {
                    return false;
                }
            }
            return isLike;
        } else {
            return false;
        }
    }

    //点赞功能
    public boolean setVideoLike(Long videoId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = userInfoService.getByUsername(auth.getName());
        VideoLike videoLike = new VideoLike();
        videoLike.setVideoId(videoId);
        videoLike.setUserId(userInfo.getId());
        boolean save = videoLikeService.save(videoLike);
        return save;
    }

    //取消点赞
    public boolean removeLike(Long videoId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = userInfoService.getByUsername(auth.getName());
        boolean remove = videoLikeService.remove(new LambdaQueryWrapper<VideoLike>()
                .eq(VideoLike::getUserId, userInfo.getId())
                .eq(VideoLike::getVideoId, videoId));
        return remove;
    }

    //计算点赞数量,通过视频id
    public int countLike(Long videoId){
        int count = videoLikeService.count(new QueryWrapper<VideoLike>().eq("video_id", videoId));
        return count;
    }
}
