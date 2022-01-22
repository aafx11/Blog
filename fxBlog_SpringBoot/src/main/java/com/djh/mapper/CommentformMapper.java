package com.djh.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.vo.commentFormVO;
import com.djh.entity.Commentform;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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

    List<commentFormVO> getLatestCommentList();

    Page<commentFormVO> getCommentListByPage(@Param("page") Page<commentFormVO> page);
}
