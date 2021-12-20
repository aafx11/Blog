package com.djh.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.vo.articleVO;
import com.djh.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
public interface ArticleMapper extends BaseMapper<Article> {

    Page<articleVO> articleListByPage(@Param("page") Page<articleVO> page, @Param("tab") String tab);

    Page<articleVO> articleListByFollow(@Param("page") Page<articleVO> page,@Param("userId") Long UserId);

    Page<articleVO> articleListByTag(@Param("page") Page<articleVO> page,@Param("tagId") Long tagId);

    Page<articleVO> articleListByUserId(@Param("page") Page<articleVO> page,@Param("userId") Long userId);


    Page<articleVO> articleListByLikeSearch(@Param("page") Page<articleVO> page,@Param("searchContent") String  searchContent);

    Page<articleVO> articleListByIdAndLike(@Param("page") Page<articleVO> page,@Param("userId") Long userId,@Param("searchContent") String  searchContent);




}
