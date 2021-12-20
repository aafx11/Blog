package com.djh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author djh
 * @since 2021-10-06
 */
@Data
public class VideoComment {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id" , type = IdType.AUTO)
    private Long id;
    private LocalDateTime created;
    private Long videoId;

    private Long userId;

    private String content;

    private Long parentId;


}
