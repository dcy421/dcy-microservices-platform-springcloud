package com.dcy.file.service;

import com.dcy.file.model.FileInfo;
import com.dcy.web.base.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dcy
 * @since 2019-09-18
 */
public interface IFileInfoService extends BaseService<FileInfo> {

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    FileInfo upload(MultipartFile file) throws Exception;


    void deleteFile(String id);
}
