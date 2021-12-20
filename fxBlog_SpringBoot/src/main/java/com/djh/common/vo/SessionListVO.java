package com.djh.common.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SessionListVO {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;
    private Long fromUserId;

    private Integer unReadCount;


    /**
     * 接收消息的用户信息
     */
    private Long toUserId;
    private String username;
    private String avatar;
    private String avatarBase64;

    private String nickname;
}
