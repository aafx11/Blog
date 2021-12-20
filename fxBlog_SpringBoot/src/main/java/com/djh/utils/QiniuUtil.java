package com.djh.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class QiniuUtil {

    public static  final String url = "qy4fxkskw.hn-bkt.clouddn.com";
    public static  final String avatarUrl = "qy8a1g6l5.hn-bkt.clouddn.com";

    @Value("2IRcWyEyoUlBDr33pAt0fZpzdY4AynWZl-REtwTo")
    private  String accessKey;
    @Value("B4t6oreiV27QlPDxX3pZ5gebY5ajHz1b0DbTo40t")
    private  String accessSecretKey;

    public  boolean upload(MultipartFile file,String fileName,String buckets){

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
//        String bucket = "blogcommunity";
        String bucket = buckets;
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
