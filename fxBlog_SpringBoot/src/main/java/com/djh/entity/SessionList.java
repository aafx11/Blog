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
 * @since 2021-09-19
 */
@Data
public class SessionList{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;
    private Long fromUserId;

    private Long toUserId;

    private Integer unReadCount;


}
