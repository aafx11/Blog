package com.djh.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author djh
 * @since 2021-08-09
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BulletinBoard implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id" , type = IdType.AUTO)
    private Long id;

    private String content;

    private LocalDateTime created;

    //1激活，0禁止
    private Integer state;


}
