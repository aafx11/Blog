package com.djh.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.mapper.*;
import com.djh.service.*;
import com.djh.utils.RandomUtil;
import com.djh.utils.RedisUtil;
import com.djh.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/*统一进行注入*/
public class BaseController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    RoleService roleService;

    @Autowired
    MenuService menuService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    BulletinBoardService bulletinBoardService;

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    FollowService followService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    ArticleLikeService articleLikeService;

    @Autowired
    ArticleCollectService articleCollectService;

    @Autowired
    CommentformService commentformService;
    @Autowired
    TagService tagService;


    @Autowired
    ArticleTagService articleTagService;

    @Autowired
    ArticleTagMapper articleTagMapper;

    @Autowired
    SessionListService sessionListService;
    @Autowired
    RandomUtil randomUtil;

    @Autowired
    MessageService messageService;

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    VideoInfoService videoInfoService;
    @Autowired
    UploadUtil uploadUtil;

    @Autowired
    VideoCollectService videoCollectService;
    @Autowired
    VideoLikeService videoLikeService;
    @Autowired
    TagsVideoService tagsVideoService;
    @Autowired
    VideoTagService videoTagService;
    @Autowired
    VideoInfoMapper videoInfoMapper;

    @Autowired
    VideoCommentService videoCommentService;

    /*获取页面信息*/
    public Page getPage() {
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int size = ServletRequestUtils.getIntParameter(request, "size", 10);
//        System.out.println("获取的current："+current);
//        System.out.println("获取的size："+size);
        return new Page(current, size);
    }

}