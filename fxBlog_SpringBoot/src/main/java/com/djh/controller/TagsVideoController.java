package com.djh.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.lang.Result;
import com.djh.entity.TagsVideo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
@RestController
@RequestMapping("/tagsVideo")
public class TagsVideoController extends BaseController {

    //获取所有视频标签
    @GetMapping("/getVideoTags")
    public Result getVideoTags( @RequestParam(value = "current", defaultValue = "1") Integer current,
                                @RequestParam(value = "size", defaultValue = "6") Integer size){
        Page<TagsVideo> videoTags = tagsVideoService.findVideoTags(new Page<>(current, size));
        return Result.success(videoTags);
    }

}
