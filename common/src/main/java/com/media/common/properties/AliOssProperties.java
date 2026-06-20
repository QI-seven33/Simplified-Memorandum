package com.media.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 配置
 * 从 application.yml 读取
 */
@Data
@Component
@ConfigurationProperties(prefix = "try.alioss")
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}