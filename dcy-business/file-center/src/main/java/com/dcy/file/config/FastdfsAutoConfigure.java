package com.dcy.file.config;

import cn.hutool.core.util.StrUtil;
import com.dcy.file.model.FileInfo;
import com.dcy.file.properties.FileServerProperties;
import com.dcy.file.service.impl.AbstractIFileInfoService;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author：dcy
 * @Description: FastDFS配置
 * @Date: 2019/9/18 13:59
 */
@Configuration
@ConditionalOnProperty(name = "dcy.file-server.type", havingValue = "fastdfs")
public class FastdfsAutoConfigure {

    @Autowired
    private FileServerProperties fileProperties;


    @Service
    public class FastdfsServiceImpl extends AbstractIFileInfoService {
        @Autowired
        private FastFileStorageClient storageClient;

        @Override
        protected String fileType() {
            return fileProperties.getType();
        }

        @Override
        protected void uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception {
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
            fileInfo.setUrl("http://" + fileProperties.getFdfs().getWebUrl() + "/" + storePath.getFullPath());
            fileInfo.setPath(storePath.getFullPath());
        }

        @Override
        protected boolean deleteFile(FileInfo fileInfo) {
            if (fileInfo != null && StrUtil.isNotEmpty(fileInfo.getPath())) {
                StorePath storePath = StorePath.parseFromUrl(fileInfo.getPath());
                storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
            }
            return true;
        }
    }
}
