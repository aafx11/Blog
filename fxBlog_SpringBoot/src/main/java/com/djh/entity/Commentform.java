package com.djh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author djh
 * @since 2021-09-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Commentform {

    private static final long serialVersionUID = 1L;

    private Long articleId;

    private Long userId;

    private String content;

    private Long parentId;

    @TableId(value = "id" , type = IdType.AUTO)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;

    List<Commentform> child;
}
