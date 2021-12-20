package com.djh.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djh.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author djh
 * @since 2021-08-16
 */
public interface TagMapper extends BaseMapper<Tag> {
   Page<Tag> tagListByPage(@Param("page") Page<Tag> page);
}
