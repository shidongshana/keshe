package com.keshe.service;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.keshe.util.ConstantPropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssService {

    public String uploadFileAvatar(MultipartFile file) {
        // 获取文件名
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + originalFilename;

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(
                ConstantPropertiesUtils.END_POINT,
                ConstantPropertiesUtils.ACCESS_KEY_ID,
                ConstantPropertiesUtils.ACCESS_KEY_SECRET);

        try {
            // 上传文件流
            InputStream inputStream = file.getInputStream();
            ossClient.putObject(ConstantPropertiesUtils.BUCKET_NAME, fileName, inputStream);

            // 关闭OSSClient
            ossClient.shutdown();

            // 返回文件的URL
            return "https://" + ConstantPropertiesUtils.BUCKET_NAME + "." + ConstantPropertiesUtils.END_POINT + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}