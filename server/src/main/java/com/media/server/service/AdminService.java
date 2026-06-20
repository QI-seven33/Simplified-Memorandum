package com.media.server.service;

import com.media.dto.AdminLoginDTO;
import com.media.entity.Admin;
import org.springframework.stereotype.Service;


public interface AdminService {

    /**
     * admin登录
     * @param adminLoginDTO
     * @return
     */
    Admin login(AdminLoginDTO adminLoginDTO);
}
