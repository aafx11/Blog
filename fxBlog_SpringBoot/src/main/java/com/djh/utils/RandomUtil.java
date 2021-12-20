package com.djh.utils;

import org.springframework.stereotype.Component;

@Component
public class RandomUtil {

    public static Long getRandom () {
        double random = Math.round((Math.random() * 9 + 1) * 1000000);
        Long id = new Double(random).longValue();
        return id;
    }
}
