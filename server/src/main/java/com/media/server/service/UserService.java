package com.media.server.service;

import com.media.dto.UserLoginDTO;
import com.media.dto.UserRegisterDTO;
import com.media.dto.UserUpdateDTO;
import com.media.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserService {


    User login(UserLoginDTO userLoginDTO);

    /**
     * 发送邮件
     */
    void sendFixedContentEmail(UserRegisterDTO userRegisterDTO);

    /**
     * 用户注册功能
     * @param registerDTO
     */
    void register(UserRegisterDTO registerDTO);

    /**
     * 根据查询用户展示信息
     * @param id
     * @return
     */
    User Information(@Param("id") Long id);

    /**
     * 修该个人资料
     * @param id
     * @param userUpdateDTO
     */
    void updateUserInfo(Long id, UserUpdateDTO userUpdateDTO);

}
