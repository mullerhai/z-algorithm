package com.zen.model.manage.util;

import com.alibaba.fastjson.JSONObject;
import com.zen.model.manage.common.exception.ValidateException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @Author: morris
 * @Date: 2020/7/6 17:24
 * @description
 * @reviewer
 */
public class FileUtil {


    public static String uploadModel(MultipartFile file,String path) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new ValidateException(50003,"未选择需上传的模型文件");
        }

        File fileUpload = new File(path);
        if (!fileUpload.exists()) {
            fileUpload.mkdirs();
        }

        fileUpload = new File(path, file.getOriginalFilename());
        if (fileUpload.exists()) {
            throw new ValidateException(500003,"上传的模型文件已存在");
        }

        try {
            file.transferTo(fileUpload);
            return fileUpload.getAbsolutePath();
        } catch (IOException e) {
            throw new ValidateException(111,"上传模型文件到服务器失败：" + e.toString());
        }
    }
}