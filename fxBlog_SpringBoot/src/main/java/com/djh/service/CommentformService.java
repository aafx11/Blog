package com.djh.service;

import com.djh.common.DTO.commentDto;
import com.djh.common.vo.commentFormVO;
import com.djh.entity.Commentform;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-09-11
 */
public interface CommentformService extends IService<Commentform> {
    List<commentFormVO> getCommentTreeByArticleId(Long articleId);

    Boolean saveComment(Authentication auth, commentDto commentDto);
}
