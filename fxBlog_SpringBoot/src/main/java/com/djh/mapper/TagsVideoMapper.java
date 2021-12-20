package com.djh.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.entity.Tag;
import com.djh.entity.TagsVideo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
public interface TagsVideoMapper extends BaseMapper<TagsVideo> {

    //获取视频标签
    Page<TagsVideo> selectVideoTags(@Param("page") Page<TagsVideo> page);
}
