package com.djh.controller;

import com.djh.common.lang.Result;
import com.djh.entity.UserInfo;
import com.djh.entity.VideoInfo;
import com.djh.utils.QiniuUtil;
import com.djh.utils.UploadUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController extends BaseController {

    @Autowired
    private QiniuUtil qiniuUtil;

    //上传文章图片或文章封面（七牛云）
    @PostMapping("/ImgUpload")
    public Result uploadArticleImg(@RequestParam("image") MultipartFile file, @RequestParam("buckets") String buckets, @RequestParam("cdn") String cdn, Principal principal) {

        System.out.println("获取的file和bucket" + file + ',' + buckets);
        String OriginalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(OriginalFileName, ".");
        System.out.println("文件名" + fileName);
        boolean upload = qiniuUtil.upload(file, fileName, buckets);
        if (upload) {
            return Result.success("上传成功", "http://" + cdn + '/' + fileName);
        } else {
            return Result.fail("上传失败");
        }

    }



    //更新头像(七牛云)
    @PostMapping("/AvatarUpload")
    public Result AvatarUpload(@RequestParam("image") MultipartFile file, @RequestParam("buckets") String buckets, @RequestParam("cdn") String cdn, Principal principal) {

        System.out.println("获取的file和bucket" + file + ',' + buckets);
        String OriginalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(OriginalFileName, ".");
        System.out.println("文件名" + fileName);
        boolean upload = qiniuUtil.upload(file, fileName, buckets);
        if (upload) {
            UserInfo userInfo = userInfoService.getByUsername(principal.getName());
            userInfo.setAvatar("http://" + cdn + '/' + fileName);
            userInfoService.updateById(userInfo);
            return Result.success("上传成功", "http://" + cdn + '/' + fileName);
        } else {
            return Result.fail("上传失败");
        }

    }
    //更换头像(base64)
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/AvatarUploadBase64")
    public Result AvatarUploadBase64(@RequestParam("image")String image){
        if (!StringUtils.isEmpty(image)){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfo user = userInfoService.getByUsername(auth.getName());
            user.setAvatarBase64(image);
            userInfoService.updateById(user);
        }

        return Result.success("更换头像");
    }

    //更换头像(保存到本地)
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/saveAvatarToLocal")
    public Result saveAvatarToLocal(@RequestParam("image") MultipartFile img){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfo user = userInfoService.getByUsername(auth.getName());
        String newFileName = uploadUtil.getNewFileName(img.getOriginalFilename());
        String path = System.getProperty("user.dir") + "/src/main/resources/static/avatar/";
        File saveImg = new File(path,newFileName);
        try {
            img.transferTo(saveImg);
            user.setAvatar(newFileName);
            userInfoService.updateById(user);

            return Result.success("上传成功",newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }

    }

    //上传文章图片到本地
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/uploadArticleImgToLocal")
    public Result uploadArticleImgToLocal(@RequestParam("img") MultipartFile img){
        String newFileName = uploadUtil.getNewFileName(img.getOriginalFilename());
        String path = System.getProperty("user.dir") + "/src/main/resources/static/articleImage/";
        File saveImg = new File(path,newFileName);
        try {
            img.transferTo(saveImg);
            return Result.success("上传成功",newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }
    }

    //上传文章封面到本地
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/uploadArticleCoverToLocal")
    public Result uploadArticleCoverToLocal(@RequestParam("image") MultipartFile img){
        String newFileName = uploadUtil.getNewFileName(img.getOriginalFilename());
        String path = System.getProperty("user.dir") + "/src/main/resources/static/articleCover/";
        File saveImg = new File(path,newFileName);
        try {
            img.transferTo(saveImg);
            return Result.success("上传成功",newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }

    }




    //视频上传到本地
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/videoUpload")
    public Result videoUpload(@RequestParam("video") MultipartFile file, @RequestParam("videoPath") String savePath,
                              @RequestParam("cover") MultipartFile cover, @RequestParam("coverPath") String coverPath,
                              @RequestParam("title") String title, @RequestParam("introduction") String introduction,
                              @RequestParam("tags") List<String> tags) {

        //获取用户信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userInfoService.getByUsername(auth.getName());
        if (auth instanceof AnonymousAuthenticationToken) {
            return Result.fail("您尚未登录");
        }

        try {
            if (videoInfoService.repeatTitle(title)) {
                return Result.fail("存在相同的标题，请修改标题");
            }

            //1.获取文件格式，生成新的文件名，防止文件重名，由前端控制传输文件的格式
            String newFileName = uploadUtil.getNewFileName(file.getOriginalFilename());
            String newCoverName = uploadUtil.getNewFileName(cover.getOriginalFilename());

            //2.保存视频到本地
            File saveFile = new File(savePath, newFileName);
            File saveCover = new File(coverPath, newCoverName);
            file.transferTo(saveFile);
            cover.transferTo(saveCover);

            //保存视频信息和视频标签
            VideoInfo videoInfo = videoInfoService.saveVideoInfo(title, introduction, newFileName, newCoverName, tags);

            if (ObjectUtils.isEmpty(videoInfo)) {
                return Result.fail("保存视频信息失败");
            }

            return Result.success("上传成功", videoInfo);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }
    }

    /**
     * @name: 重新上传视频或封面文件，先删除之前的视频文件,以及更新数据库中视频信息
     * @description:
     * @param file
     * @param savePath
     * @param cover
     * @param coverPath
     * @param videoId
     * @return com.djh.common.lang.Result
     */
    @PreAuthorize("hasAuthority('hasLogin')")
    @PostMapping("/reUploadByVideoId")
    public Result reUploadByVideoId(@RequestParam(value = "video",required = false ) MultipartFile file, @RequestParam(value = "videoPath",required = false) String savePath,
                                    @RequestParam(value = "cover",required = false)  MultipartFile cover, @RequestParam(value = "coverPath",required = false) String coverPath,
                                    @RequestParam(value = "videoId")String  videoId) {

        System.out.println("视频"+file);
        System.out.println("封面"+cover);
        long id = Long.parseLong(videoId);
        try {
            String url = "";

            //1.重新上传视频文件,返回新文件的路径
            if (file != null) {
                url = videoInfoService.reUploadVideoOrCoverByVideoId(id, file, savePath, "video");
            }

            //2.重新上传封面,返回新文件的路径
            if (cover != null) {
                url = videoInfoService.reUploadVideoOrCoverByVideoId(id,cover,coverPath,"cover");
            }

            return Result.success(null,url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }

    }
}
