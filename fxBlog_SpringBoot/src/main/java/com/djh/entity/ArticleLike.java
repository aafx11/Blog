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
 * @since 2021-09-08
 */
@Data
public class ArticleLike {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id" , type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long articleId;


}
