package com.djh.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.vo.VideoVO;
import com.djh.common.vo.articleVO;
import com.djh.entity.VideoInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
public interface VideoInfoMapper extends BaseMapper<VideoInfo> {

    Page<VideoVO> selectVideoListByUserId(@Param("page") Page<VideoVO> page, @Param("userId") Long userId,@Param("searchContent") String  searchContent);

    VideoVO selectVideoDetailByVideoId(@Param("videoId") Long videoId);

    Page<VideoVO> selectVideoListByState(@Param("page") Page<VideoVO> page, @Param("tab") String tab,@Param("searchContent")String searchContent);

    //查找已过审视频按播放量排序
    Page<VideoVO> selectVideoOrderByView(@Param("page") Page<VideoVO> page);

    //查找已过审视频按日期排序
    Page<VideoVO> selectVideoOrderByCreated(@Param("page") Page<VideoVO> page);

    //模糊查询已过审的视频
    Page<VideoVO> selectVideoByLike(@Param("page") Page<VideoVO> page,@Param("searchContent")String searchContent);

    //获取关注的用户已过审的视频
    Page<VideoVO> selectVideoByFollow(@Param("page") Page<VideoVO> page,@Param("userId") Long UserId);

    //通过视频标签查找视频
    Page<VideoVO> selectVideoByTag(@Param("page") Page<VideoVO> page,@Param("tagId") Long tagId);

}
