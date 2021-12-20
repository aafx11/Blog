package com.djh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.lang.Result;
import com.djh.common.vo.VideoVO;
import com.djh.entity.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
@RestController
@RequestMapping("/videoInfo")
public class VideoInfoController extends BaseController {


    /**
     * @name: 分页获取个人的视频列表,通过用户id
     * @description:
     * @param current
     * @param size
     * @param searchContent
     * @return com.djh.common.lang.Result
     */
    @PreAuthorize("hasAuthority('hasLogin')")
    @GetMapping("/getVideoListByUserId")
    public Result getVideoListByUserId(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "searchContent")String searchContent){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo = userInfoService.getByUsername(auth.getName());
            Long userId = userInfo.getId();
            //获取视频信息和对应标签
            Page<VideoVO> videoList = videoInfoService.findVideoListByUserId(new Page<>(current,size),userId,searchContent);

            //判断是否点赞和收藏了视频,统计视频的点赞和收藏数量
            videoList.getRecords().forEach(video->{
                video.setHasLike(videoLikeService.hasLike(video.getId()));
                video.setHasCollect(videoCollectService.hasCollect(video.getId()));
                video.setLikeCount(videoLikeService.countLike(video.getId()));
                video.setCollects(videoCollectService.countCollect(video.getId()));
            });


            return Result.success(videoList);
        }else {
            return Result.fail("您尚未登录");
        }
    }


    /**
     * @name: 获取视频列表通过审核状态或者模糊查询
     * @description:
     * @param current
     * @param size
     * @param searchContent
     * @param tab
     * @return com.djh.common.lang.Result
     */
    @GetMapping("/getVideoByStateOrLike")
    public Result getVideoByStateOrLike(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                                        @RequestParam(value = "searchContent")String searchContent,
                                        @RequestParam(value = "tab",defaultValue = "all")String tab){


        //1.获取视频信息和对应标签
        Page<VideoVO> videoList = videoInfoService.findVideoByStateOrLike(new Page<>(current, size), tab, searchContent);

        //2.判断是否点赞和收藏了视频,统计视频的点赞和收藏数量
        videoList.getRecords().forEach(video->{
            video.setHasLike(videoLikeService.hasLike(video.getId()));
            video.setHasCollect(videoCollectService.hasCollect(video.getId()));
            video.setLikeCount(videoLikeService.countLike(video.getId()));
            video.setCollects(videoCollectService.countCollect(video.getId()));
        });

        return Result.success(videoList);
    }



    /**
     * @name: 获取所有已过审视频，通过观看数排序
     * @description:
     * @param current
     * @param size
     * @return com.djh.common.lang.Result
     */
    @GetMapping("/getVideoOrderByView")
    public Result getVideoOrderByView(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size){
        System.out.println("size"+size);
        Page<VideoVO> videoOrderByView = videoInfoService.findVideoOrderByView(new Page<>(current, size));
        videoOrderByView.getRecords().forEach(video ->{
            videoInfoService.setVideoLikeAndCollect(video);
        });
        return Result.success(videoOrderByView);
    }

    /**
     * @name: 获取所有已过审视频，通过日期排序
     * @description:
     * @param current
     * @param size
     * @return com.djh.common.lang.Result
     */
    @GetMapping("/getVideoOrderByCreated")
    public Result getVideoOrderByCreated(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size){

        Page<VideoVO> videoOrderByView = videoInfoService.findVideoOrderByCreated(new Page<>(current, size));
        videoOrderByView.getRecords().forEach(video ->{
            videoInfoService.setVideoLikeAndCollect(video);
        });
        return Result.success(videoOrderByView);
    }

    /**
     * @name: 模糊查询已过审的视频
     * @description:
     * @param current
     * @param size
     * @return com.djh.common.lang.Result
     */
    @GetMapping("/getVideoOrderByLike")
    public Result getVideoOrderByLike(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      @RequestParam(value = "searchContent")String searchContent){

        Page<VideoVO> videoOrderByView = videoInfoService.findVideoByLike(new Page<>(current, size),searchContent);
        videoOrderByView.getRecords().forEach(video ->{
            videoInfoService.setVideoLikeAndCollect(video);
        });
        return Result.success(videoOrderByView);
    }


    /**
     * @name: 获取关注的用户已过审的视频
     * @description:
     * @param current
     * @param size
     * @return com.djh.common.lang.Result
     */
    @GetMapping("/getVideoByFollow")
    public Result getVideoByFollow(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserInfo userInfo = userInfoService.getByUsername(auth.getName());
            List<Follow> follows = followService.list(new QueryWrapper<Follow>().eq("user_id", userInfo.getId()));
            if (!ObjectUtils.isEmpty(follows)){
                Page<VideoVO> videoByFollow = videoInfoService.findVideoByFollow(new Page<>(current, size), userInfo.getId());
                videoByFollow.getRecords().forEach(videoVO -> {
                    videoInfoService.setVideoLikeAndCollect(videoVO);
                });
                return Result.success(videoByFollow);
            } else {
                return Result.fail("您尚未关注任何人");
            }
        } else {
            return Result.fail("您尚未登录");
        }
    }


    /**
     * @name: 通过视频标签获取视频
     * @description:
     * @param tagId
     * @param current
     * @param size
     * @return com.djh.common.lang.Result
     */
    @GetMapping("/getVideoByTag")
    public Result getVideoByTag(@RequestParam(value = "tagId") Long tagId,
                                   @RequestParam(value = "current", defaultValue = "1") Integer current,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size){
        Page<VideoVO> videoByTag = videoInfoService.findVideoByTag(new Page<>(current, size), tagId);
        videoByTag.getRecords().forEach(videoVO -> {
            videoInfoService.setVideoLikeAndCollect(videoVO);
        });
        return Result.success(videoByTag);
    }

    /**
     * @name:获取视频的详细信息包括作者信息
     * @description:
     * @param videoId
     * @return com.djh.common.lang.Result
     */
    @GetMapping("/getVideoDetailByVideoId")
    public Result getVideoDetailByVideoId(Long videoId){
        VideoVO videoDetail = videoInfoService.findVideoDetailByVideoId(videoId);
        //判断是否点赞和收藏了视频,统计视频的点赞和收藏数量
        videoInfoService.setVideoLikeAndCollect(videoDetail);
        //为已经通过审核的视频，增加播放量
        videoInfoService.setVideoView(videoDetail);

        return Result.success(videoDetail);
    }

    /**
     * @name: 修改视频标题，简介，标签
     * @description:
     * @param videoId
     * @param title
     * @param introduction
     * @param tags
     * @return com.djh.common.lang.Result
     */
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/setVideoInfoByVideoId")
    public Result setVideoInfoByVideoId(@RequestParam(value = "videoId")String videoId,
                                        @RequestParam(value = "title",required = false)String title,
                                        @RequestParam(value = "introduction",required = false)String introduction,
                                        @RequestParam(value = "tags",required = false)List<String> tags){


        VideoInfo videoInfo = videoInfoService.getById(videoId);

        if (title!=null){
//            if (videoInfoService.repeatTitle(title)) {
//                return Result.fail("存在相同的标题，请修改标题");
//            }
           videoInfo.setTitle(title);
        }

        if (introduction!=null){
            videoInfo.setIntroduction(introduction);
        }

        if (tags!=null){
            List<TagsVideo> newVideoTags = tagsVideoService.createNewVideoTags(tags);
            videoTagService.createVideoTags(videoInfo.getId(),newVideoTags);
        }

        videoInfoService.updateById(videoInfo);
        return Result.success("修改成功");
    }


    /**
     * @name: 修改视频审核状态以及审核评语,通过视频id
     * @description:
     * @param videoId
     * @param state
     * @param examine
     * @return com.djh.common.lang.Result
     */
    @PreAuthorize("hasAuthority('video:examine')")
    @PostMapping("/setVideoExamine")
    public Result setVideoExamine(@RequestParam(value = "videoId") Long videoId,
                                  @RequestParam(value = "state") int state,
                                  @RequestParam(value = "examine") String examine){

        videoInfoService.update(new UpdateWrapper<VideoInfo>().eq("id",videoId)
                                                            .set("state",state)
                                                            .set("examine",examine));
        return Result.success("修改成功");
    }


    /**
     * @name: 根据视频id删除视频信息以及对应文件
     * @description:
     * @param videoIds
     * @return com.djh.common.lang.Result
     */
    @PreAuthorize("hasAuthority('video:delete')")
    @Transactional
    @PostMapping("/deleteVideoByVideoId")
    public Result deleteVideoByVideoId(@RequestBody Long[] videoIds){

        for (int i = 0;i<videoIds.length;i++){
            Long id = videoIds[i];

            videoInfoService.removeVideoInfoAndFile(id);
        }
        return Result.success("删除成功");
    }
}
