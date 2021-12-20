package com.djh.service.impl;

import com.djh.entity.Test;
import com.djh.mapper.TestMapper;
import com.djh.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-10-14
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

}
