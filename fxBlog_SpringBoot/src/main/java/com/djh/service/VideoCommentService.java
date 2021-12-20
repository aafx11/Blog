package com.djh.service;

import com.djh.common.DTO.VideoCommentDto;
import com.djh.common.vo.VideoCommentVO;
import com.djh.entity.VideoComment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-10-06
 */
public interface VideoCommentService extends IService<VideoComment> {

    List<VideoCommentVO> getCommentTreeByVideoId(Long videoId);

    Boolean saveComment(Authentication auth, VideoCommentDto commentDto);
}
