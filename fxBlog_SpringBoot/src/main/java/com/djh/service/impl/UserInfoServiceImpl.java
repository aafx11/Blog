package com.djh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.djh.common.vo.UserProfileVO;
import com.djh.entity.*;
import com.djh.mapper.ArticleMapper;
import com.djh.mapper.FollowMapper;
import com.djh.mapper.UserInfoMapper;
import com.djh.service.MenuService;
import com.djh.service.RoleService;
import com.djh.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djh.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    RoleService roleService;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    MenuService menuService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    FollowMapper followMapper;

    @Override
    public UserInfo getByUsername(String username) {
        return getOne(new QueryWrapper<UserInfo>().eq("username",username));
    }

    @Override
    public UserInfo getByNickname(String nickname){
        return getOne(new QueryWrapper<UserInfo>().eq("nickname",nickname));
    }
    /*获取角色权限，包括用户角色和菜单权限：
    *   一.先判断redis缓存内，是否存有该用户权限信息
    *       1.存有，直接获取
    *       2.不存有，重新获取权限信息
    *   二.获取用户角色信息:
    *       1.通过用户id，获取role_id角色id，通过角色id，获取相对应的角色信息
    *       2.判断是否获取到角色信息，获取到后，将信息转换为流，getCode获取编码,加上ROLE_前缀，将编码用","隔开，添加进authority
    *   三.获取菜单权限
    *       1.用户角色表左连接角色菜单表，通过用户角色表的user_id获取角色菜单表的menu_id
    *       2.获取到菜单id后，通过菜单id获取菜单权限的详细信息
    *       3.用逗号隔开添加进authority
    *       4.将权限信息存入redis
    * */
    @Override
    public String getUserAuthority(Long userId) {
        UserInfo userInfo =userInfoMapper.selectById(userId);
        String authority ="";

        if(redisUtil.hasKey("Authority:"+userInfo.getUsername())){
            authority = (String)redisUtil.get("Authority:"+userInfo.getUsername());


            System.out.println("从redis获取权限"+authority);
        }else {
            //获取用户角色信息
            List<Role> roles = roleService.list(new QueryWrapper<Role>().inSql("id","select role_id from user_role where user_id = " +userId));
            if(roles.size() > 0){
                String roleCodes=roles.stream().map(r ->"ROLE_"+ r.getCodes()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }

            //获取菜单信息
            List<Long> menuIds = userInfoMapper.getNavMenuByUserId(userId);
            if (menuIds.size() > 0) {
                List<Menu> UserMenus = menuService.listByIds(menuIds);
                String MenuPerms=UserMenus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
                authority = authority.concat(MenuPerms);
            }
            System.out.println("重新获取权限"+authority);
            //将获取到的权限信息存入redis(键，值，时间秒)
            redisUtil.set("Authority:"+userInfo.getUsername(),authority,3600);
        }


        return authority;
    }

    //获取作者的信息
    @Override
    public UserProfileVO getUserProfileById(Long userId) {
        UserProfileVO userProfileVO = new UserProfileVO();
        UserInfo userInfo = userInfoService.getById(userId);
//        UserInfo userInfo = baseMapper.selectById(userId);
//        System.out.println("userinfo"+userInfo);


        BeanUtils.copyProperties(userInfo,userProfileVO);
        userProfileVO.setUserId(userId);

//        System.out.println("Vo"+userProfileVO);
        //计算作者用户的文章总数
        int articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getUserId,userId));
        userProfileVO.setArticleCount(articleCount);

        //计算作者用户的粉丝总数
        int followerCount = followMapper.selectCount(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId,userId));
        userProfileVO.setFansCount(followerCount);

        return userProfileVO;
    }

    @Override
    public void clearRedisAuthority(String username) {
        redisUtil.del("Authority:"+ username);
    }


    /*当用户角色发生改变时，删除原存储在redis中的权限信息：
    *   1.通过roleId获取到所有相关的用户信息
    *   2.删除每个相关用户在redis中缓存的权限信息
    *
    * */
    @Override
    public void clearRedisAuthorityByRoleId(Long roleId) {
        System.out.println("执行清除缓存");
        List<UserInfo> userInfos=this.list(new QueryWrapper<UserInfo>()
                .inSql("id","select user_id from user_role where role_id = " +roleId));

        System.out.println("获取的userInfos:"+userInfos);
        userInfos.forEach(userInfo -> {
            this.clearRedisAuthority(userInfo.getUsername());
        });
    }

    @Override
    public void clearRedisAuthorityByMenuId(Long menuId) {
        System.out.println("获取的菜单id"+menuId);
        List<UserInfo> userInfos=userInfoMapper.listByMenuId(menuId);
        System.out.println("获取的用户信息"+userInfos);

        userInfos.forEach(userInfo -> {
            System.out.println(userInfo.getUsername());

            this.clearRedisAuthority(userInfo.getUsername());

        });
    }

    //当删除用户时候，删除其关注和粉丝信息，删除点赞和收藏信息，删除其发布的视频信息
}
