package com.djh.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.djh.common.DTO.VideoCommentDto;
import com.djh.common.vo.VideoCommentVO;
import com.djh.entity.Commentform;
import com.djh.entity.UserInfo;
import com.djh.entity.VideoComment;
import com.djh.mapper.VideoCommentMapper;
import com.djh.service.UserInfoService;
import com.djh.service.VideoCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-10-06
 */
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment> implements VideoCommentService {

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    VideoCommentService videoCommentService;


    /**
     * @name: 通过视频id找到所有评论，并排列成树状
     * @description:
     * @param videoId
     * @return java.util.List<com.djh.common.vo.VideoCommentVO>
     */
    public List<VideoCommentVO> getCommentTreeByVideoId(Long videoId) {

        List<VideoCommentVO> commentList = this.baseMapper.getCommentListByVideoId(videoId);
        Map<Long, VideoCommentVO> map = new HashMap<>();
        List<VideoCommentVO> result = new ArrayList<>();

        //找到所有根节点
        for (VideoCommentVO c : commentList) {
            if (c.getParentId() == null) {
                result.add(c);
            }
            map.put(c.getId(), c);
        }

        for (VideoCommentVO c : commentList) {
            //找到该节点的父节点
            if (c.getParentId() != null) {
                VideoCommentVO parent = map.get(c.getParentId());
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


    /**
     * @name: 保存视频评论
     * @description:
     * @param auth
     * @param commentDto
     * @return java.lang.Boolean
     */
    public Boolean saveComment(Authentication auth, VideoCommentDto commentDto){
        UserInfo user = userInfoService.getByUsername(auth.getName());

        VideoComment commentform = new VideoComment();
        commentform.setVideoId(commentDto.getVideoId());
        commentform.setUserId(user.getId());
        commentform.setContent(commentDto.getContent());
        commentform.setCreated(LocalDateTime.now());
        if (!ObjectUtils.isNull(commentDto.getParentId())){
            commentform.setParentId(commentDto.getParentId());
        }

        boolean save = videoCommentService.save(commentform);
        return save;
    }
}
