package com.dcy.file.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2019/9/18 13:33
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "dcy.file-server")
@RefreshScope
public class FileServerProperties {
    /**
     * 为以下3个值，指定不同的自动化配置
     * qiniu：七牛oss
     * aliyun：阿里云oss
     * fastdfs：本地部署的fastDFS
     */
    private String type;

    /**
     * oss配置
     */
    OssProperties oss = new OssProperties();

    /**
     * fastDFS配置
     */
    FdfsProperties fdfs = new FdfsProperties();
}
