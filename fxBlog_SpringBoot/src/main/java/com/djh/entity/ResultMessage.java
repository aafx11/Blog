package com.djh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//服务端发送给客户端
public class ResultMessage {
    private boolean isSystem;
    private String fromName;
    //private String toName;
    private Object message;
}