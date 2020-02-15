package com.dcy.file.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2019/9/18 13:33
 */
@Getter
@Setter
public class OssProperties {
    /**
     * 密钥key
     */
    private String accessKey;
    /**
     * 密钥密码
     */
    private String accessKeySecret;
    /**
     * 端点
     */
    private String endpoint;
    /**
     * bucket名称
     */
    private String bucketName;
    /**
     * 说明
     */
    private String domain;
}
