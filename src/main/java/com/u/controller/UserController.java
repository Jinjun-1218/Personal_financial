package com.u.controller;


import com.u.constant.JwtClaimsConstant;
import com.u.constant.MessageConstant;
import com.u.domain.dto.LoginDto;
import com.u.domain.dto.RegisterDto;
import com.u.domain.dto.ResetPasswordDto;
import com.u.domain.po.User;
import com.u.domain.vo.AccountInfoVo;
import com.u.properties.JwtProperties;
import com.u.result.Result;
import com.u.service.IUserService;
import com.u.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import com.u.domain.dto.UpdatePasswordDto;
/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login_v1")
    @ApiOperation("用户登录")
    @Deprecated
    public Result<String> login_v1(@RequestBody LoginDto loginDto) {
        return Result.error("登录版本已更新，请使用新版本");
    }
    @PostMapping("/login_v2")
    @ApiOperation("用户登录")
    public Result<String> login_v2(@RequestBody LoginDto loginDto) {
        log.info("用户登录：{}", loginDto);

        User user = userService.login(loginDto);
        if (user != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
            claims.put(JwtClaimsConstant.USERNAME, user.getUsername());
            claims.put(JwtClaimsConstant.ACOUNTNAME, user.getAccountName());
            String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);  //jwt包含了当前员工的信息
            return Result.success(token); //将jwt令牌响应给前端
        } else {
            return Result.error(MessageConstant.LOGIN_FAILED);
        }
    }
    /**
     * 注册接口
     *
     * @param registerUserDto 用户注册数据
     * @return 注册成功或失败信息
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result<String> register(@RequestBody RegisterDto registerUserDto) {
        log.info("用户注册：{}", registerUserDto);
        try {
            boolean result = userService.register(registerUserDto);
            if (result) {
                return Result.success(MessageConstant.REGISTER_SUCCESS);
            } else {
                return Result.error(MessageConstant.REGISTER_FAILED);
            }
        } catch (Exception e) {
            return Result.error(MessageConstant.REGISTER_FAILED + e.getMessage());
        }
    }

    /**
     * 忘记密码接口
     *
     * @param resetPasswordDto 封装手机号和重置后的密码
     * @return 重置密码成功或失败信息
     */
    @PostMapping("/resetPassword")
    @ApiOperation("忘记密码")
    public Result<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        log.info("用户重置密码：{}", resetPasswordDto);

        try {
            boolean result = userService.resetPassword(resetPasswordDto);
            if (result) {
                return Result.success(MessageConstant.RESET_PASSWORD_SUCCESS);
            } else {
                return Result.error(MessageConstant.RESET_PASSWORD_FAILED);
            }
        } catch (Exception e) {
            return Result.error(MessageConstant.RESET_PASSWORD_FAILED + e.getMessage());
        }
    }

    /**
     * 获取账号信息
     *
     * @return
     */
    @GetMapping("/accountInfo")
    @ApiOperation("获取账号信息")
    public Result<AccountInfoVo> getAccountInfo() {
        log.info("获取当前用户信息");

        AccountInfoVo accountInfo = userService.getCurrentAccountInfo();

        if (accountInfo != null) {
            return Result.success(accountInfo);
        } else {
            return Result.error(MessageConstant.USER_NOT_EXIST);
        }
    }
    /**
     * 修改密码
     *
     * @return
     */
    @PostMapping("/updatePassword")
    @ApiOperation("修改密码")
    public Result<String> updatePassword(@RequestBody UpdatePasswordDto updateDto) {
        log.info("用户修改密码");
        try {
            if (updateDto.getAccountName() != null) {
                return Result.error("非法参数：账号名称不可修改");
            }
            boolean result = userService.updatePassword(updateDto);
            return result ? Result.success(MessageConstant.PASSWORD_UPDATE_SUCCESS)
                    : Result.error(MessageConstant.PASSWORD_UPDATE_FAILED);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/loginWithPhoneNumber")
    @ApiOperation("用户手机号登录")
    public Result<String> loginWithPhoneNumber(@RequestParam String phoneNumber) {
        log.info("用户手机号登录，手机号：{}", phoneNumber);

        // 根据手机号查询用户
        User user = userService.loginWithPhoneNumber(phoneNumber);
        if (user != null) {
            // 校验手机号后成功登录，生成 JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
            claims.put(JwtClaimsConstant.USERNAME, user.getUsername());
            claims.put(JwtClaimsConstant.ACOUNTNAME, user.getAccountName());

            // 生成 JWT 令牌
            String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

            // 返回 JWT 令牌给前端
            return Result.success(token);
        } else {
            // 用户未找到，手机号不正确
            return Result.error(MessageConstant.LOGIN_FAILED);
        }
    }
}
