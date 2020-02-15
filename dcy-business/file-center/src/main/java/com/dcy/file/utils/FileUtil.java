package com.dcy.file.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.dcy.file.model.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2019/9/18 13:34
 */
@Slf4j
public class FileUtil {
    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static FileInfo getFileInfo(MultipartFile file) {
        String md5 = null;
        try {
            md5 = fileMd5(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInfo fileInfo = new FileInfo();
        // 将文件的md5设置为文件表的id
        fileInfo.setId(md5);
        fileInfo.setName(file.getOriginalFilename());
        fileInfo.setContentType(file.getContentType());
        fileInfo.setIsImg(fileInfo.getContentType().startsWith("image/"));
        fileInfo.setSize(file.getSize());
        fileInfo.setCreateDate(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN));
        return fileInfo;
    }

    /**
     * 文件的md5
     *
     * @param inputStream
     * @return
     */
    public static String fileMd5(InputStream inputStream) {
        try {
            return DigestUtils.md5Hex(inputStream);
        } catch (IOException e) {
            log.error("fileMd5-error", e);
        }
        return null;
    }

    public static String saveFile(MultipartFile file, String path) {
        try {
            File targetFile = new File(path);
            if (targetFile.exists()) {
                return path;
            }
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            file.transferTo(targetFile);
            return path;
        } catch (Exception e) {
            log.error("saveFile-error", e);
        }
        return null;
    }

    public static boolean deleteFile(String pathname) {
        File file = new File(pathname);
        if (file.exists()) {
            boolean flag = file.delete();
            if (flag) {
                File[] files = file.getParentFile().listFiles();
                if (files == null || files.length == 0) {
                    file.getParentFile().delete();
                }
            }
            return flag;
        }
        return false;
    }
}
