package com.sky.controller.admin;

import com.sky.config.OssConfiguration;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("上传文件到阿里云oss")
    public Result<String> upload(MultipartFile file){// 参数名字与前端一致
        log.info("上传文件到阿里云oss");
        try {
            String originalFilename = file.getOriginalFilename();
            String extention = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String url = aliOssUtil.upload(file.getBytes(), UUID.randomUUID().toString() + extention);
            return Result.success(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
