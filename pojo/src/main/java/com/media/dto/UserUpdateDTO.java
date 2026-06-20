package com.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 用户信息更新 DTO
 * 用于：保存个人资料修改
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private String userName;

    private String bio;

    /** 头像 OSS URL（前端上传后获得） */
    private String avatar;

    private String oldPassword;

    private String newPassword;
}