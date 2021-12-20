package com.djh.mapper;

import com.djh.common.vo.commentFormVO;
import com.djh.entity.Commentform;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author djh
 * @since 2021-09-11
 */
public interface CommentformMapper extends BaseMapper<Commentform> {
    List<commentFormVO> getCommentListByArticleId(Long articleId);
}
