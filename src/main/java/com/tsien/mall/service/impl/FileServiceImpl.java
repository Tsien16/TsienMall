package com.tsien.mall.service.impl;

import com.google.common.collect.Lists;
import com.tsien.mall.service.FileService;
import com.tsien.mall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/30 0030 23:31
 */

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 上传文件
     *
     * @param file file
     * @param path path
     * @return 上传结果
     */
    @Override
    public String upload(MultipartFile file, String path) {

        String fileName = file.getOriginalFilename();

        // 获取文件扩展名,并且用UUID组装一个新的名字，保证上传的名字相同的文件，处理后名字不相同
        String fileExtensionName = fileName != null ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;

        logger.info("开始上传文件，上传文件的文件名:{},上传的路径:{},新的文件名:{}", fileName, path, uploadFileName);

        // 创建文件夹，并设置文件夹为可写状态
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            boolean setResult = fileDir.setWritable(true);
            if (setResult) {
                logger.info("设置文件夹可写权限成功");
            } else {
                logger.info("设置文件夹可写权限失败");
            }

            boolean makeResult = fileDir.mkdirs();
            if (makeResult) {
                logger.info("创建文件夹成功");
            } else {
                logger.info("创建文件夹失败");
            }
        }

        File targetFile = new File(path, uploadFileName);

        /*
          先把文件上传到工程目录的upload目录下
          然后将文件上传到ftp服务器上
          上传完后删除工程目录upload目录下的文件
         */
        try {
            file.transferTo(targetFile);
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            boolean deleteResult = targetFile.delete();
            if (deleteResult) {
                logger.info("删除文件成功");
            } else {
                logger.info("删除文件失败");
            }

        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }

        return targetFile.getName();

    }


}
