package com.djh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.DTO.ArticleDto;
import com.djh.common.vo.articleVO;
import com.djh.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.djh.entity.UserInfo;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
public interface ArticleService extends IService<Article> {


    Page<articleVO> getList(Page<articleVO> page, String tab);

    Article edit(ArticleDto articleDto, UserInfo userInfo);

    Map<String,Object> getArticleDetailsById (Long articleId);

    Page<articleVO> getListByFollow(Page<articleVO> page, Long userId);

    Page<articleVO> getListByTag(Page<articleVO> page,Long tagId);

    Page<articleVO> getListByUserId(Page<articleVO> page,Long userId);

    Boolean hasLike(Authentication authentication, Long articleId);

    Page<articleVO> getArticleListByLikeSearch(Page<articleVO> page,String searchContent);

    Page<articleVO> getListByIdAndLike(Page<articleVO> page, Long userId,String content);

}
