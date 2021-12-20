package com.djh.service;

import com.djh.common.DTO.MenuDto;
import com.djh.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author djh
 * @since 2021-07-11
 */
public interface MenuService extends IService<Menu> {

    List<MenuDto> getUserNav();

    List<Menu> tree();

}
