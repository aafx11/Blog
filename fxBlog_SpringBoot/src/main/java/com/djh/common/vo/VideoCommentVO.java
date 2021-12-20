package com.djh.common.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class VideoCommentVO {

    private static final long serialVersionUID = 1L;

    private Long videoId;

    private Long userId;

    private String nickname;
    private String username;
    private String avatar;
    private String avatarBase64;


    private String content;
    private Integer score;
    private Long parentId;

    @TableId(value = "id" , type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;

    private String parentNickname;
    List<VideoCommentVO> child;
}
