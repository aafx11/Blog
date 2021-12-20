package com.djh.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.lang.Result;
import com.djh.entity.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
@RestController
@RequestMapping("/tag")
public class TagController extends BaseController {

    @GetMapping("/getTagList")
    public Result getTagList( @RequestParam(value = "current", defaultValue = "1") Integer current,
                              @RequestParam(value = "size", defaultValue = "6") Integer size){
        Page<Tag> list = tagService.getTagListByPage(new Page<>(current,size));
        return Result.success(list);

    }


}
