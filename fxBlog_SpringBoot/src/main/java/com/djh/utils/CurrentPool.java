package com.djh.utils;

import com.djh.controller.WebSocketService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//统一存储webSocket和session
public class CurrentPool {

    public static Map<Long, WebSocketService> webSocket = new ConcurrentHashMap<>();

    public static Map<Long, List<Object>> sessionPool = new ConcurrentHashMap<>();
}
