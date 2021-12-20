package com.djh.common.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String introduction;
    private String content;
    private List<String> tags;
    private String cover;
    private String coverBase64;





}
