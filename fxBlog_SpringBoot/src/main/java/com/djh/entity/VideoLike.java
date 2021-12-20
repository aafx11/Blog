package com.djh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author djh
 * @since 2021-09-27
 */
@Data
public class VideoLike  {


    @TableId(value = "id" , type = IdType.AUTO)
    private Long id;
    private Long videoId;

    private Long userId;


}
