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

public class Message  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;
    private LocalDateTime created;
    private Long fromUserId;

    private String fromNickname;

    private Long toUserId;

    private String toNickname;

    private String content;

    /**
     * 1为已读，0为未读
     */
    private Integer readFlag;

    private String fromAvatar;


}
