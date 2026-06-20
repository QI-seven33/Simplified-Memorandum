package com.media.server.controller.admin;

import com.media.common.properties.JwtProperties;
import com.media.common.result.Result;
import com.media.common.utils.JwtUtil;
import com.media.dto.AdminLoginDTO;
import com.media.entity.Admin;
import com.media.server.service.AdminService;
import com.media.vo.AdminLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.Jar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/admin/admin1")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 登录
     * @param adminLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<AdminLoginVO> login(@RequestBody AdminLoginDTO adminLoginDTO){
        log.info("管理员登录：{}", adminLoginDTO);
        Admin admin = adminService.login(adminLoginDTO);

        /**
         * claims 命名含义
         * claims 直译为 "声明" 或 "权利要求"，在 JWT（JSON Web Token）中是标准术语，
         * 特指 JWT 的载荷部分（Payload）
         */
        //登录成功后，生成jwt令牌
        // 1. 创建一个 Map 对象，用于存储要放入令牌的数据
        Map<String, Object> claims = new HashMap<>();
        // 2. 向 Map 中添加数据：管理员ID
        claims.put("adminId", admin.getId());
        // 3. 调用工具类生成 JWT 令牌
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),  // 密钥
                jwtProperties.getAdminTtl(),        // 过期时间
                claims);                            // 要存入的数据

        AdminLoginVO adminLoginVO = AdminLoginVO.builder()
                .id(admin.getId())
                .userName(admin.getUsername())
                .token(token)
                .build();

        return Result.success(adminLoginVO);
    }

}
