package com.media.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateVO implements Serializable {

    private String userName;

    private String bio;

    /** 头像 OSS URL（前端上传后获得） */
    private String avatar;

    private String password;


}