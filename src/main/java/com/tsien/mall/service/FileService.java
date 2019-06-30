package com.tsien.mall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/30 0030 23:30
 */

public interface FileService {

    /**
     * 上传文件
     *
     * @param file file
     * @param path path
     * @return 上传结果
     */
    String upload(MultipartFile file, String path);
}
