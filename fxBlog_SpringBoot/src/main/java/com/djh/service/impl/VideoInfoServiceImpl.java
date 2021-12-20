package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.vo.VideoVO;
import com.djh.common.vo.articleVO;
import com.djh.entity.*;
import com.djh.mapper.TagsVideoMapper;
import com.djh.mapper.VideoInfoMapper;
import com.djh.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djh.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
@Service
public class VideoInfoServiceImpl extends ServiceImpl<VideoInfoMapper, VideoInfo> implements VideoInfoService {

    @Autowired
    VideoInfoService videoInfoService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    TagsVideoService tagsVideoService;
    @Autowired
    TagsVideoMapper tagsVideoMapper;
    @Autowired
    VideoTagService videoTagService;
    @Autowired
    VideoInfoMapper videoInfoMapper;
    @Autowired
    VideoLikeService videoLikeService;
    @Autowired
    VideoCollectService videoCollectService;
    @Autowired
    FollowService followService;
    @Autowired
    UploadUtil uploadUtil;

    //存在相同标题返回true
    @Override
    public Boolean repeatTitle(String title) {
        VideoInfo isRepeat = videoInfoService.getOne(new QueryWrapper<VideoInfo>().eq("title", title));
        if (!ObjectUtils.isEmpty(isRepeat)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @param title
     * @param introduction
     * @param videoUrl
     * @param coverUrl
     * @param tags
     * @return com.djh.entity.VideoInfo
     * @name:保存视频信息和对应标签信息到数据库
     * @description:
     */
    @Override
    public VideoInfo saveVideoInfo(String title, String introduction, String videoUrl, String coverUrl, List<String> tags) {
        //保存视频信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = userInfoService.getByUsername(auth.getName());
        VideoInfo videoInfo = VideoInfo.builder()
                .title(title)
                .introduction(introduction)
                .coverUrl(coverUrl)
                .videoUrl(videoUrl)
                .userId(userInfo.getId())
                .state(0)
                .created(LocalDateTime.now())
                .examine("无")
                .build();
        videoInfoService.save(videoInfo);

        //保存视频标签
        if (!ObjectUtils.isEmpty(tags)) {
            //没有的标签创建新的
            List<TagsVideo> tagsVideos = tagsVideoService.createNewVideoTags(tags);
            //视频与标签中间表
            videoTagService.createVideoTags(videoInfo.getId(), tagsVideos);
        }

        //发布视频后，用户积分增加1
        int score = userInfo.getScore() + 1;
        userInfo.setScore(score);
        userInfoService.updateById(userInfo);

        return videoInfo;
    }

    /**
     * @param page
     * @param userId
     * @param searchContent
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.djh.common.vo.VideoVO>
     * @name: 通过用户id查找视频列表
     * @description:
     */
    public Page<VideoVO> findVideoListByUserId(Page<VideoVO> page, Long userId, String searchContent) {

        Page<VideoVO> videoList = videoInfoMapper.selectVideoListByUserId(page, userId, searchContent);
        setVideoTags(videoList);
        return videoList;
    }

    /**
     * @name: 通过视频的审核状态或者模糊查询查找视频信息
     * @description:
     * @param page
     * @param tab
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.djh.common.vo.VideoVO>
     */
    public Page<VideoVO> findVideoByStateOrLike(Page<VideoVO> page,String tab,String searchContent){

        Page<VideoVO> videoVOPage = videoInfoMapper.selectVideoListByState(page, tab,searchContent);
        setVideoTags(videoVOPage);
        return videoVOPage;
    }


    /**
     * @name: 查找已过审视频按播放量排序
     * @description:
     * @param page
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.djh.common.vo.VideoVO>
     */
    public Page<VideoVO> findVideoOrderByView(Page<VideoVO> page){

        Page<VideoVO> videoVOPage = videoInfoMapper.selectVideoOrderByView(page);
        setVideoTags(videoVOPage);
        return videoVOPage;
    }

    /**
     * @name: 查找已过审视频按日期排序
     * @description:
     * @param page
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.djh.common.vo.VideoVO>
     */
    public Page<VideoVO> findVideoOrderByCreated(Page<VideoVO> page) {
        Page<VideoVO> videoVOPage = videoInfoMapper.selectVideoOrderByCreated(page);
        setVideoTags(videoVOPage);
        return videoVOPage;
    }

    /**
     * @name: 模糊查询已过审视频
     * @description:
     * @param page
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.djh.common.vo.VideoVO>
     */
    public Page<VideoVO> findVideoByLike(Page<VideoVO> page,String searchContent) {
        Page<VideoVO> videoVOPage = videoInfoMapper.selectVideoByLike(page,searchContent);
        setVideoTags(videoVOPage);
        return videoVOPage;
    }


    /**
     * @name: 获取关注的用户已过审的视频
     * @description:
     * @param page
     * @param userId
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.djh.common.vo.VideoVO>
     */
    public Page<VideoVO> findVideoByFollow(Page<VideoVO> page,Long userId){

        Page<VideoVO> videoVOPage = this.baseMapper.selectVideoByFollow(page, userId);
        setVideoTags(videoVOPage);
        return videoVOPage;
    }


    /**
     * @name: 通过视频标签获取视频
     * @description:
     * @param page
     * @param tagId
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.djh.common.vo.VideoVO>
     */
    public Page<VideoVO> findVideoByTag(Page<VideoVO> page,Long tagId){

        Page<VideoVO> videoVOPage = this.baseMapper.selectVideoByTag(page, tagId);
        setVideoTags(videoVOPage);
        return videoVOPage;
    }

    /**
     * @param videoId
     * @return com.djh.common.vo.VideoVO
     * @name: 通过视频id，获取视频信息详情，包括相关作者信息
     * @description:
     */
    public VideoVO findVideoDetailByVideoId(Long videoId) {
        VideoVO videoVO = videoInfoMapper.selectVideoDetailByVideoId(videoId);
        List<VideoTag> videoTags = videoTagService.findTagsByVideoId(videoId);
        if (!videoTags.isEmpty()) {
            List<Long> tagsId = videoTags.stream().map(VideoTag::getTagId).collect(Collectors.toList());
            List<TagsVideo> tags = tagsVideoMapper.selectBatchIds(tagsId);
            videoVO.setTags(tags);
        }
        return videoVO;
    }


    /**
     * @param videoVO
     * @return void
     * @name:判断是否点赞和收藏了视频,统计视频的点赞和收藏数量
     * @description:
     */
    public void setVideoLikeAndCollect(VideoVO videoVO) {
        videoVO.setHasLike(videoLikeService.hasLike(videoVO.getId()));
        videoVO.setHasCollect(videoCollectService.hasCollect(videoVO.getId()));
        videoVO.setLikeCount(videoLikeService.countLike(videoVO.getId()));
        videoVO.setCollects(videoCollectService.countCollect(videoVO.getId()));
    }

    /**
     * @param videoVO
     * @return void
     * @name: 已通过审核的视频，才可以增加播放量
     * @description:
     */
    public void setVideoView(VideoVO videoVO) {
        if (videoVO.getState() == 1) {
            videoVO.setView(videoVO.getView() + 1);
            videoInfoService.update(new UpdateWrapper<VideoInfo>()
                    .eq("id", videoVO.getId())
                    .setSql("view = view + 1"));
        }
    }

    /**
     * @param videoList
     * @return void
     * @name:为视频找到相对于的标签
     * @description:
     */
    private void setVideoTags(Page<VideoVO> videoList) {
        videoList.getRecords().forEach(video -> {
            List<VideoTag> videoTags = videoTagService.findTagsByVideoId(video.getId());
            if (!videoTags.isEmpty()) {
                List<Long> tagsId = videoTags.stream().map(VideoTag::getTagId).collect(Collectors.toList());
                List<TagsVideo> tags = tagsVideoMapper.selectBatchIds(tagsId);
                video.setTags(tags);
            }
        });
    }

    /**
     * @name: 重新上传视频或封面文件，并删除原来的文件和信息
     * @description:
     * @param videoId
     * @param file
     * @param savePath
     * @return String
     */
    public String reUploadVideoOrCoverByVideoId(Long videoId, MultipartFile file,String savePath,String tab){

        try{

            //1.保存新视频或新封面到本地
            String newFileName = uploadUtil.getNewFileName(file.getOriginalFilename());
            File saveFile = new File(savePath,newFileName);
            file.transferTo(saveFile);

            //2.删除原来的视频或者封面文件,保存新的路径信息
            VideoInfo videoInfo = videoInfoService.getById(videoId);
            String url = "" ;
            if (!ObjectUtils.isEmpty(videoInfo)){

                if (tab == "video"){
                    System.out.println("视频路线");
                    String originalFileUrl = "D:/Student/Vue_student/Blog/fxBlog_SpringBoot/src/main/resources/static/video/" + videoInfo.getVideoUrl();
                    File originalFile = new File(originalFileUrl);

                    if (originalFile.exists()){
                        System.out.println("删除");
                        originalFile.delete();

                    }
                    videoInfoService.update(new UpdateWrapper<VideoInfo>()
                            .eq("id",videoId)
                            .set("video_url",newFileName));
                    VideoInfo newVideoInfo = videoInfoService.getById(videoId);
                    url =  newVideoInfo.getVideoUrl();
                }

                if (tab == "cover"){
                    System.out.println("封面路线");

                    String originalFileUrl = "D:/Student/Vue_student/Blog/fxBlog_SpringBoot/src/main/resources/static/videoCover/" + videoInfo.getCoverUrl();
                    File originalFile = new File(originalFileUrl);

                    if (originalFile.exists()){
                        originalFile.delete();

                    }
                    videoInfoService.update(new UpdateWrapper<VideoInfo>()
                            .eq("id",videoId)
                            .set("cover_url",newFileName));
                    VideoInfo newVideoInfo = videoInfoService.getById(videoId);
                    url =  newVideoInfo.getCoverUrl();
                }

            }
          return url;

        } catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }

    }

    /**
     * @name: 通过视频id，删除视频相关信息，以及视频文件
     * @description:
     * @param videoId
     * @return void
     */
    public void removeVideoInfoAndFile(Long videoId){
        System.out.println("获取的id"+videoId);

        //先删除文件，再删除信息
        VideoInfo videoInfo = videoInfoService.getById(videoId);
//        VideoInfo videoInfo = videoInfoService.getOne(new QueryWrapper<VideoInfo>().eq("id", videoId));

        System.out.println("获取视频"+videoInfo);

        String videoFileUrl = "D:/Student/Vue_student/Blog/fxBlog_SpringBoot/src/main/resources/static/video/" + videoInfo.getVideoUrl();
        File videoFile = new File(videoFileUrl);

        String coverFileUrl = "D:/Student/Vue_student/Blog/fxBlog_SpringBoot/src/main/resources/static/videoCover/" + videoInfo.getCoverUrl();
        File coverFile = new File(coverFileUrl);

        if (videoFile.exists()){
            System.out.println("删除视频文件");
            videoFile.delete();
        }
        if (coverFile.exists()){
            System.out.println("删除封面文件");
            coverFile.delete();
        }

        videoTagService.remove(new QueryWrapper<VideoTag>().in("video_id",videoId));
        videoLikeService.remove(new QueryWrapper<VideoLike>().in("video_id",videoId));
        videoCollectService.remove(new QueryWrapper<VideoCollect>().in("video_id",videoId));
        videoInfoService.removeById(videoId);
    }



}
