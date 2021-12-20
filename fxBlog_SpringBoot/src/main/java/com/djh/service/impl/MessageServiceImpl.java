package com.djh.service.impl;

import com.djh.entity.Message;
import com.djh.mapper.MessageMapper;
import com.djh.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djh
 * @since 2021-09-19
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
