package com.tsien.mall.util;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/1 0001 0:00
 */

public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpPort = PropertiesUtil.getProperty("ftp.server.port");
    private static String ftpuser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPassword = PropertiesUtil.getProperty("ftp.pass");
    private static String ftpDir = PropertiesUtil.getProperty("ftp.dir");

    public static boolean uploadFile(List<File> fileList) throws IOException {

        FTPUtil ftpUtil = new FTPUtil(ftpIp, Integer.parseInt(ftpPort), ftpuser, ftpPassword);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile(ftpDir, fileList);
        logger.info("开始连接ftp服务器，结束上传，上传结果:{}", result);

        return result;

    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fileInputStream = null;

        if (connectFtpServer(this.getIp(), this.getPort(), this.getUser(), this.getPassword())) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList) {
                    fileInputStream = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fileInputStream);
                }

            } catch (IOException e) {
                logger.error("上传文件异常", e);
                uploaded = false;
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                ftpClient.disconnect();
            }
        }
        return uploaded;


    }

    /**
     * 连接ftp服务器
     *
     * @param ip       ip
     * @param port     port
     * @param user     user
     * @param password password
     * @return 连接是否成功
     */
    private boolean connectFtpServer(String ip, int port, String user, String password) {

        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            isSuccess = ftpClient.login(user, password);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常", e);
        }

        return isSuccess;
    }

    @Getter
    @Setter
    private String ip;

    @Getter
    @Setter
    private int port;

    @Getter
    @Setter
    private String user;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private FTPClient ftpClient;

    private FTPUtil(String ip, int port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }


}
