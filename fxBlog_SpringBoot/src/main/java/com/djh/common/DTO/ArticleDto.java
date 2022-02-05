package com.djh.common.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Long id;

    private String title;
    private String introduction;
    private String content;
    private List<String> tags;
    private String cover;
    private String coverBase64;





}
