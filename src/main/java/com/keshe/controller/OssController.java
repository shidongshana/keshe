package com.keshe.controller;

import com.keshe.entity.RestBean;
import com.keshe.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/fileoss")
public class OssController {

    @Autowired
    private OssService ossService;

    @PostMapping("/uploadOssFile")
    public RestBean<String> uploadOssFile(MultipartFile file) {
        try {
            String url = ossService.uploadFileAvatar(file);
            return RestBean.success(url);
        } catch (Exception e) {
            return RestBean.failure(500, "上传失败：" + e.getMessage());
        }
    }
}