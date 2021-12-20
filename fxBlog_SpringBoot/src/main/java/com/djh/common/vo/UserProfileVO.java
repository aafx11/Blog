package com.djh.common.vo;

import lombok.Data;

import java.sql.Blob;

@Data
public class UserProfileVO {

    private Long userId;

    private String username;

    private String avatar;

    private String nickname;

    //用户关注的人数
    private Integer followCount;

    //用户的粉丝数量
    private Integer fansCount;

    //用户的文章数量
    private Integer articleCount;

    //专栏数量
    private Integer columns;

    //评论数量
    private Integer commentCount;

    private String email;

    private String gender;

    private String phone;

    private String  avatarBase64;



    //简介
    private String briefintroduction;
}
