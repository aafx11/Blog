package com.djh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.vo.VideoVO;
import com.djh.entity.VideoInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
public interface VideoInfoService extends IService<VideoInfo> {

    //存在相同标题返回true
    Boolean repeatTitle(String title);
    //保存视频信息
    VideoInfo saveVideoInfo(String title, String introduction, String videoUrl, String coverUrl, List<String> tags);

    //通过用户id查找视频列表
    Page<VideoVO> findVideoListByUserId(Page<VideoVO> page, Long userId,String searchContent);

    //查找已过审视频按播放量排序
    Page<VideoVO> findVideoOrderByView(Page<VideoVO> page);

    //查找已过审视频按日期排序
    Page<VideoVO> findVideoOrderByCreated(Page<VideoVO> page);

    //模糊查询已过审视频
    Page<VideoVO> findVideoByLike(Page<VideoVO> page,String searchContent);

    //获取关注的用户已过审的视频
    Page<VideoVO> findVideoByFollow(Page<VideoVO> page,Long userId);

    //通过视频id，获取视频信息详情，包括相关作者信息
    VideoVO findVideoDetailByVideoId(Long videoId);

    //判断是否点赞和收藏了视频,统计视频的点赞和收藏数量
    void setVideoLikeAndCollect(VideoVO videoVO);

    //已通过审核的视频，才可以增加播放量
    void setVideoView(VideoVO videoVO);

    //重新上传视频文件，并删除原来的文件和信息
    String reUploadVideoOrCoverByVideoId(Long videoId, MultipartFile file,String savePath,String tab);
    //通过视频的审核状态或者模糊查询查找视频信息
    Page<VideoVO> findVideoByStateOrLike(Page<VideoVO> page,String tab,String searchContent);

    //通过视频id，删除视频相关信息，以及视频文件
    void removeVideoInfoAndFile(Long videoId);

    //通过视频标签获取视频
    Page<VideoVO> findVideoByTag(Page<VideoVO> page,Long tagId);

}
