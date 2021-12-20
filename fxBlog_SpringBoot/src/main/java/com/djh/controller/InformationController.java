package com.djh.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.djh.common.lang.Result;
import com.djh.entity.BulletinBoard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 *  信息控制器
 * </p>
 *
 * @author djh
 * @since 2021-08-09
 */
@RestController
@RequestMapping("/information")
public class InformationController extends BaseController {

    @GetMapping("/getNotices")
    public Result getNotices(){
        List<BulletinBoard> list = bulletinBoardService.list(new LambdaQueryWrapper<BulletinBoard>()
                .eq(BulletinBoard::getState,true));
        return Result.success(list.get(list.size()-1));
    }
}
