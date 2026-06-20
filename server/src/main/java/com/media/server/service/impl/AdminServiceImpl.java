package com.media.server.service.impl;

import com.media.common.constant.MessageConstant;
import com.media.common.exception.AccountNotFoundException;
import com.media.dto.AdminLoginDTO;
import com.media.entity.Admin;
import com.media.server.mapper.AdminMapper;
import com.media.server.service.AdminService;
import com.media.vo.AdminLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(AdminLoginDTO adminLoginDTO) {
        String username = adminLoginDTO.getUsername();
        String password = adminLoginDTO.getPassword();

        //根据用户名查询数据库
        Admin admin = adminMapper.getByUsername(username);
        //加盐
        String salt = "mdxqnjqn";

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (admin == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND_OR_PASSWORD_ERROR);
        }

        //密码比对
        //对前端传入的明文密码进行md5加密后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes()) + salt;
        if (!password.equals(admin.getPassword())) {//employee.getPassword()：从数据库查出来的加密密码,!password.equals(...)：如果不相等，说明密码错误
            throw new AccountNotFoundException(MessageConstant.PASSWORD_ERROR);
        }
//        if (admin.getStatus() == StatusConstant.DISABLE) {
//            //账号被锁定
//            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
//        }


//        // 3. 检查密码是否正确（重要！）
//        if (!admin.getPassword().equals(password)) {
//            return null;  // 密码错误
//        }
        return admin;
    }
}
