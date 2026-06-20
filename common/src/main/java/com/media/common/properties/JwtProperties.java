package com.media.common.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "try.jwt")
public class JwtProperties {
    /**
     * 管理员生成jwt令牌相关配置
     */
    private String adminSecretKey;
    //adminTtl 是 Admin Time To Live 的缩写，直译为 "管理员令牌存活时间"
    private long adminTtl;
    private String adminTokenName;

    /**
     * 用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
