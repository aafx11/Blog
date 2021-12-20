package com.djh.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class commentVO {

    //评论id
    private Long id;

    private String content;

    private Long userId;

    private String nickname;
    private String avatarBase64;

    private String avatar;

    //用户积分
    private Integer score;

    private Long articleId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;
    private LocalDateTime updated;
}
