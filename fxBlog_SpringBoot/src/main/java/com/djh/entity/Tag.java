package com.djh.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

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
@Accessors(chain = true)
public class Tag  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id" ,type = IdType.AUTO)
    private Long id;

    private String name;

    /**
     * 关联话题
     */
    @Builder.Default
    private Integer articleCount = 1;


}
