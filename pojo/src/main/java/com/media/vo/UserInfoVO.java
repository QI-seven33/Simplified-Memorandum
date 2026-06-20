package com.media.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户信息查询返回对象
 * 用于：GET /user/user1/{id} 接口
 * 包含用户展示所需的完整信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO implements Serializable {

    private Long id;

    private String userName;

    private String avatar;   // 头像URL（阿里云OSS）

    private String bio;      // 个性签名

}