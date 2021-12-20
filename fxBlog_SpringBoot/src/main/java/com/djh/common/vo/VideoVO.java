package com.djh.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.djh.entity.TagsVideo;
import com.djh.entity.VideoTag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 头像
     */
    private String avatar;
    private String avatarBase64;

    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 账号
     */
    private String username;
    /**
     * 视频简介
     */
    private String introduction;
    /**
     * 标题
     */
    private String title;
    /**
     * 评论统计
     */
    private Integer comments;
    /**
     * 收藏次數
     */
    private Integer collects;
    /**
     * 浏览量
     */
    private Integer view;
    /**
     * 视频标签
     */
    private List<TagsVideo> tags;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;
    /**
     * 点赞数量
     */
    private Integer likeCount;
    /**
     * 是否点赞
     */
    private Boolean hasLike;
    /**
     * 是否收藏
     */
    @TableField(exist = false)
    private Boolean hasCollect;

    /**
     * 0审核中，1通过审核，2禁用
     */
    private Integer state;

    private String coverUrl;
    private String videoUrl;
    /**
     * 审核评语
     */
    private String examine;
}
