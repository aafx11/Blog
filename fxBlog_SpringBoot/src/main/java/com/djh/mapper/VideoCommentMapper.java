package com.djh.mapper;

import com.djh.common.vo.VideoCommentVO;
import com.djh.common.vo.commentFormVO;
import com.djh.entity.VideoComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author djh
 * @since 2021-10-06
 */
public interface VideoCommentMapper extends BaseMapper<VideoComment> {
    List<VideoCommentVO> getCommentListByVideoId(Long videoId);
}
