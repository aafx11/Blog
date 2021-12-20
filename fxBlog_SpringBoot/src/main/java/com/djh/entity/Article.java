package com.djh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id" , type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    /**
     * 文章简介
     */
    private String introduction;

    private Long userId;

    /**
     * 评论数
     */
    private Integer comments;

    /**
     * 收藏数
     */
    private Integer collects;

    private Integer view ;

    /**
     * 是否置顶,1代表置顶
     */
    private Boolean top;

    /**
     * 是否加精
     */
    private Boolean essence;

    /**
     * 专栏id
     */
    private Integer sectionId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;
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
    @TableField(exist = false)
    private Boolean hasLike;
    /**
     * 是否收藏
     */
    @TableField(exist = false)
    private Boolean hasCollect;
}


