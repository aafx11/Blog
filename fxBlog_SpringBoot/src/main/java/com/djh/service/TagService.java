package com.djh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
public interface TagService extends IService<Tag> {
    List<Tag> insertTags(List<String> tags);

    Page<Tag> getTagListByPage(Page<Tag> page);
}
