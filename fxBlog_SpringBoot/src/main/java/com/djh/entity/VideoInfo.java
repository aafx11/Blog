package com.djh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("video_info")
public class VideoInfo  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id" , type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;

    private String title;

    private String introduction;

    /**
     * 审核评语
     */
    private String examine;

    private Long userId;

    private Integer comments;

    private Integer collects;

    private Integer view;


    private Integer likeCount;

    /**
     * 0审核中，1通过审核，2审核不通过
     */
    private Integer state;

    private String coverUrl;
    private String videoUrl;

}
