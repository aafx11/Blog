package com.djh.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.djh.common.DTO.commentDto;
import com.djh.common.vo.commentFormVO;
import com.djh.entity.Commentform;
import com.djh.entity.UserInfo;
import com.djh.mapper.CommentformMapper;
import com.djh.service.CommentformService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djh.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-09-11
 */
@Service
public class CommentformServiceImpl extends ServiceImpl<CommentformMapper, Commentform> implements CommentformService {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    CommentformService commentformService;

    public List<commentFormVO> getCommentTreeByArticleId(Long articleId) {
        List<commentFormVO> commentList = this.baseMapper.getCommentListByArticleId(articleId);
        Map<Long, commentFormVO> map = new HashMap<>();
        List<commentFormVO> result = new ArrayList<>();

        //找到所有根节点
        for (commentFormVO c : commentList) {
            if (c.getParentId() == null) {
                result.add(c);
            }
            map.put(c.getId(), c);
        }

        for (commentFormVO c : commentList) {
            //找到该节点的父节点
            if (c.getParentId() != null) {
                commentFormVO parent = map.get(c.getParentId());
                if (parent == null) {
                    continue;
                }

                if (parent.getId().intValue() == c.getId().intValue()){
                    continue;
                }

                if (parent.getChild() == null){
                    parent.setChild(new ArrayList<>());
                }
                c.setParentNickname(parent.getNickname());
                parent.getChild().add(c);
            }
        }
        return result;
    }

    public Boolean saveComment(Authentication auth, commentDto commentDto){
        UserInfo user = userInfoService.getByUsername(auth.getName());
        System.out.println("文章id"+commentDto.getArticleId());
        Commentform commentform = new Commentform();
        commentform.setArticleId(commentDto.getArticleId());
        commentform.setUserId(user.getId());
        commentform.setContent(commentDto.getContent());
        commentform.setCreated(LocalDateTime.now());
        if (!ObjectUtils.isNull(commentDto.getParentId())){
            commentform.setParentId(commentDto.getParentId());
        }

        boolean save = commentformService.save(commentform);
        return save;
    }
}
