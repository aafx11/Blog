package com.djh.common.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoCommentDto implements Serializable {
    private Long parentId;

    private Long videoId;

    private String content;
}
