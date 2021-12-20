package com.djh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
@Data
public class ArticleTag  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id" ,type = IdType.AUTO)
    private Long id;

    private Long articleId;

    private Long tagId;


}
