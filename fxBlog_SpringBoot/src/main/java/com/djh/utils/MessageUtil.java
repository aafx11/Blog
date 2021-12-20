package com.djh.utils;

import com.djh.entity.ResultMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageUtil {

    //封装消息
    public static String getMessageData(boolean isSystemMessage, String fromName, Object message) {
        try {
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.setSystem(isSystemMessage);
            resultMessage.setMessage(message);
            if (fromName != null) {
                resultMessage.setFromName(fromName);
            }
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(resultMessage);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
}
