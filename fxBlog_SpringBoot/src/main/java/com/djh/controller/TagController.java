package com.djh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.common.lang.Result;
import com.djh.entity.Article;
import com.djh.entity.ArticleTag;
import com.djh.entity.Tag;
import com.djh.mapper.ArticleTagMapper;
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
        list.getRecords().forEach(item ->{
            QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
            wrapper.eq("tag_id",item.getId());
            Integer count = articleTagMapper.selectCount(wrapper);
            item.setArticleCount(count);
        });
        return Result.success(list);

    }


}
