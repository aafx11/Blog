package com.djh.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.djh.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class articleVO implements Serializable {

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
    private String  avatarBase64;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 文章简介
     */
    private String introduction;
    /**
     * 账号
     */
    private String username;
    /**
     * 标题
     */
    private String title;
    /**
     * 评论统计
     */
    private Integer comments;
    /**
     * 置顶
     */
    private Boolean top;
    /**
     * 加精
     */
    private Boolean essence;
    /**
     * 收藏次數
     */
    private Integer collects;
    /**
     * 话题关联标签
     */
    private List<Tag> tags;
    /**
     * 浏览量
     */
    private Integer view;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;
    /**
     * 修改时间
     */
    private LocalDateTime updated;
    /**
     * 文章封面
     */
    private String cover;
    private String coverBase64;

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


}
