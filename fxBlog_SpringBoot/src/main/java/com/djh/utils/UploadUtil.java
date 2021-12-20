package com.djh.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UploadUtil {

    public String getNewFileName(String originalFileName){
        return UUID.randomUUID().toString()+ "." + StringUtils.substringAfterLast(originalFileName,".");
    }
}
