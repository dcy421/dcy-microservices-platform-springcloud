package com.dcy.file.config;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.dcy.file.model.FileInfo;
import com.dcy.file.properties.FileServerProperties;
import com.dcy.file.service.impl.AbstractIFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author：dcy
 * @Description: 阿里云配置
 * @Date: 2019/9/18 13:51
 */
@Configuration
@ConditionalOnProperty(name = "dcy.file-server.type", havingValue = "aliyun")
public class AliyunOSSAutoConfigure {

    @Autowired
    private FileServerProperties fileProperties;

    /**
     * 阿里云配置
     *
     * @return
     */
    @Bean
    public OSSClient ossClient() {
        OSSClient ossClient = new OSSClient(fileProperties.getOss().getEndpoint()
                , new DefaultCredentialProvider(fileProperties.getOss().getAccessKey(), fileProperties.getOss().getAccessKeySecret())
                , null);
        return ossClient;
    }

    @Service
    public class AliyunOssServiceImpl extends AbstractIFileInfoService {
        @Autowired
        private OSSClient ossClient;

        @Override
        protected String fileType() {
            return fileProperties.getType();
        }

        @Override
        protected void uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception {
            ossClient.putObject(fileProperties.getOss().getBucketName(), fileInfo.getName(), file.getInputStream());
            fileInfo.setUrl(fileProperties.getOss().getDomain() + "/" + fileInfo.getName());
        }

        @Override
        protected boolean deleteFile(FileInfo fileInfo) {
            ossClient.deleteObject(fileProperties.getOss().getBucketName(), fileInfo.getName());
            return true;
        }
    }

}
