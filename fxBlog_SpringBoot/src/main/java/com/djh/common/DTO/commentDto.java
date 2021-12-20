package com.djh.common.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class commentDto implements Serializable {
    private Long parentId;

    private Long articleId;

    private String content;

}
