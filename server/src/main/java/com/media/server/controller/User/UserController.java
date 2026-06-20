package com.media.server.controller.User;

import com.media.common.constant.MessageConstant;
import com.media.common.context.BaseContext;
import com.media.common.properties.JwtProperties;
import com.media.common.result.Result;
import com.media.common.utils.JwtUtil;
import com.media.dto.UserLoginDTO;
import com.media.dto.UserRegisterDTO;
import com.media.dto.UserUpdateDTO;
import com.media.entity.User;
//import com.media.server.service.OssService;
import com.media.server.service.UserService;
import com.media.vo.DiaryVO;
import com.media.vo.UserInfoVO;
import com.media.vo.UserLoginVO;
import com.media.vo.UserUpdateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.media.common.utils.AliOssUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户控制器
 * 处理用户相关接口：登录、注册、信息查询、OSS 上传凭证
 */
@Slf4j
@RestController
@RequestMapping("/user/user1")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private JwtProperties jwtProperties;

//    @Autowired
//    private OssService ossService;  // 新增：OSS 上传服务

    // ==================== 登录注册 ====================

    /**
     * 用户登录
     * 流程：验证用户名密码 → 生成 JWT Token → 返回用户基本信息
     *
     * @param userLoginDTO 登录参数（username, password）
     * @return 登录结果（id, userName, token）
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO);

        // 1. 验证登录
        User user = userService.login(userLoginDTO);


        if (user != null) {
            // 2. 构建 JWT claims
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());

            // 3. 生成 JWT Token
            String token = JwtUtil.createJWT(
                    jwtProperties.getUserSecretKey(),
                    jwtProperties.getUserTtl(),
                    claims
            );

            // 4. 构建返回对象
            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .id(user.getId())
                    .userName(user.getUsername())
                    .token(token)
                    .build();
            return Result.success(userLoginVO);

        }
        return Result.error(MessageConstant.ACCOUNT_NOT_FOUND_OR_PASSWORD_ERROR);
    }

    /**
     * 发送验证码邮件
     * 用于注册时验证邮箱
     *
     * @param userRegisterDTO 包含邮箱信息
     * @return 发送结果
     */
    @PostMapping("/sendCode")
    public Result<String> sendCode(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("发送验证码到邮箱：{}", userRegisterDTO);
        userService.sendFixedContentEmail(userRegisterDTO);
        return Result.success("邮件发送成功");
    }

    /**
     * 用户注册
     *
     * @param registerDTO 注册参数（username, password, email, code）
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody UserRegisterDTO registerDTO) {
        log.info("用户注册: {}", registerDTO);
        userService.register(registerDTO);
        return Result.success();
    }

    // ==================== 用户信息 ====================

    /**
     * 查询用户信息（头像、昵称、签名）
     * 用于：登录成功后同步用户资料到前端
     * 需要携带 JWT Token（由请求拦截器验证）
     *
     * @param id 用户ID（路径参数）
     * @return 用户信息（不含敏感信息）
     */
    @GetMapping("/info/{id}")
    public Result<UserInfoVO> userInfo(@PathVariable Long id) {
        log.info("查询用户信息，id为 {}", id);

        User user = userService.Information(id);

        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .avatar(user.getAvatar())    // OSS 头像 URL
                .bio(user.getBio())          // 个性签名
                .build();

        return Result.success(userInfoVO);
    }

    // ========== 新增：头像上传 ==========

    /**
     * 上传头像到 OSS
     * 后端代理上传，前端传文件，后端调用阿里云 OSS
     *
     * @param file 图片文件
     * @return OSS 图片 URL
     */
    @PostMapping("/upload-avatar")
    public Result<String> uploadAvatar(MultipartFile file) {
        Long userId = BaseContext.getCurrentId();
        log.info("用户 {} 上传头像", userId);

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // ✅ 构建新的文件名称：Sce_try/目录 + userId_时间戳 + 后缀
            String objectName = "Sce_try/" + userId + "_" + System.currentTimeMillis() + extension;
            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(),objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传对象",e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    // ==================== OSS 上传 ====================

    /**
     * 获取阿里云 OSS 临时上传凭证（STS）
     * 用于前端直传头像到 OSS
     * 限制：只能上传到 avatars/{userId}/ 目录，有效期 1 小时
     *
     * @return STS 临时凭证（accessKeyId, accessKeySecret, securityToken）
     */
//    @GetMapping("/oss-sts")
//    public Result<OssStsVO> getOssStsToken() {
//        // 从 ThreadLocal 获取当前登录用户ID（由 JWT 拦截器设置）
//        Long userId = BaseContext.getCurrentId();
//        log.info("用户 {} 请求 OSS STS 凭证", userId);
//
//        OssStsVO vo = ossStsService.generateStsToken(userId);
//        return Result.success(vo);
//    }

    @PutMapping("infoUpdate/{id}")
    public Result<UserUpdateVO> updateUser(@PathVariable Long id,
                                           @RequestBody UserUpdateDTO userUpdateDTO) {
        // 验证：只能修改自己的信息
        Long currentUserId = BaseContext.getCurrentId();
        if (!currentUserId.equals(id)) {
            return Result.error("无权修改他人信息");
        }

        log.info("用户 {} 更新资料", id);
        userService.updateUserInfo(id, userUpdateDTO);
        return Result.success();
    }

    /**
     * 获取日记列表
     * 用于：左侧栏展示所有日记
     * 需要携带 JWT Token（由请求拦截器验证)
     * @param
     * @return
     */
    @GetMapping("diary/list")
    public Result<DiaryVO> getDiaryList(@RequestAttribute Long userId) {
        log.info("查询日记列表，userId为 {}", userId);


        return null;
    }
}