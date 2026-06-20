package com.media.server.service.impl;

import com.media.common.constant.MessageConstant;
import com.media.common.exception.AccountNotFoundException;
import com.media.common.result.Result;
import com.media.dto.UserLoginDTO;
import com.media.dto.UserRegisterDTO;
import com.media.dto.UserUpdateDTO;
import com.media.entity.User;
import com.media.server.mapper.UserMapper;
import com.media.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        //根据用户名查询数据库
        User user = userMapper.getByUsername(username);
        //加盐
        String salt = "mdxqnjqn";

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (user == null) {
            //账号不存在
//           throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
            return null;
        }
        //密码比对
        //对前端传入的明文密码进行md5加密后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes()) + salt;
        if (!password.equals(user.getPassword())) {//employee.getPassword()：从数据库查出来的加密密码,!password.equals(...)：如果不相等，说明密码错误
//            throw new AccountNotFoundException(MessageConstant.PASSWORD_ERROR);
            return null;
        }
//        if (admin.getStatus() == StatusConstant.DISABLE) {
//            //账号被锁定
//            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
//        }
//        // 3. 检查密码是否正确（重要！）
//        if (!admin.getPassword().equals(password)) {
//            return null;  // 密码错误
//        }
        return user;
    }

    @Override
    public void sendFixedContentEmail(UserRegisterDTO userRegisterDTO) {
        String to = userRegisterDTO.getEmail();
        // 邮箱为空时，直接返回，不发送
        if (to == null || to.trim().isEmpty()) {
            log.warn("邮箱为空，跳过发送");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("亲爱的展博：");
            message.setText("当你看到这封信的时候，我已经在去机场的路上了，请原谅我改签了航班，我知道，如果我看到你们，肯定就走不了了，因为我舍不得你们每一个人，尤其是你，和大家在一起的日子，是我一生中最快乐的时光，虽然我也不想结束，但是新的故事总要开始，展博，过去我不懂，爱是什么，是你让我明白，爱是当你爱上一个人会舍弃自己的自由换取他的自由，爱是当你爱上一个人会改变自己的人生成全他的心愿，爱是当你爱上一个人会愿意放开手留下最好的回忆和祝福，爱情最美的不一定是终点，旅途一起走过，也已不负一生，原谅我的天真，这是我能想到的，最好的结局。爱你的宛瑜。至于验证码是“SBWLK”");
            mailSender.send(message);
            log.info("邮件发送成功: {}", to);
        } catch (Exception e) {
            // 只记录错误，不抛异常，不影响主流程
            log.error("邮件发送失败: {}, 错误: {}", to, e.getMessage());
            // 不抛异常，Controller 会返回"发送成功"，实际上失败了
        }
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        User user = new User();

        //对象属性拷贝
        BeanUtils.copyProperties(userRegisterDTO,user);

        //设置账号状态，默认活跃
        user.setStatus("active");
        //设置默认角色
        user.setRole("user");
        //校验用户名是否已存在
        User existingUser = userMapper.getByUsername(userRegisterDTO.getUsername());
        if (existingUser != null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_LOCKED);
        }
        //加盐
        String salt = "mdxqnjqn";
        String VsPassword = DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes());
        String passwordWithSalt = VsPassword + salt;
        user.setPassword(passwordWithSalt);
        // 7. 设置创建时间和更新时间
        user.setCreatedAt(LocalDateTime.now());
        userMapper.register(user);

        log.info("用户注册成功: {}", userRegisterDTO.getUsername());
    }

    /**
     * 查询用户信息
     * 用于：个人资料展示
     */
    public User Information(Long id) {
        log.info("查询用户信息，id: {}", id);
        User user = userMapper.getInformation(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    /**
     * 个人信息修改功能
     * @param id
     * @param dto
     */
    public void updateUserInfo(Long id, UserUpdateDTO dto) {
        log.info("更新用户信息，id: {}", id);

        // 查询现有用户
        User existingUser = userMapper.getInformation(id);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        User user = new User();
        user.setId(id);
        user.setUsername(dto.getUserName());
        user.setBio(dto.getBio());
        user.setAvatar(dto.getAvatar());

        // 密码可选：不为空才修改
        if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {
            log.info("原密码: {}", dto.getOldPassword());
            log.info("新密码: {}", dto.getNewPassword());
            // 1. 校验原密码是否为空
            log.info("数据库密码: {}", existingUser.getPassword());
            if (dto.getOldPassword() == null || dto.getOldPassword().isEmpty()) {
                throw new RuntimeException("请输入原密码");
            }

            // 2. 加密用户输入的原密码
            String salt = "mdxqnjqn";
            String oldMd5 = DigestUtils.md5DigestAsHex(dto.getOldPassword().getBytes());
            String oldPasswordWithSalt = oldMd5 + salt;

            // 3. 比对数据库中的原密码
            if (!oldPasswordWithSalt.equals(existingUser.getPassword())) {
                throw new RuntimeException("原密码错误");
            }

            // 4. 加密新密码
            String newMd5 = DigestUtils.md5DigestAsHex(dto.getNewPassword().getBytes());
            user.setPassword(newMd5 + salt);
        }

        // 5. 更新数据库
        int rows = userMapper.updateUserInfo(user);
        if (rows == 0) {
            throw new RuntimeException("更新失败");
        }

        log.info("用户信息更新成功，id: {}", id);
    }


}
