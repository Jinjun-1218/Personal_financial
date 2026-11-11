package com.u.service;

import com.u.domain.dto.LoginDto;
import com.u.domain.dto.RegisterDto;
import com.u.domain.dto.ResetPasswordDto;
import com.u.domain.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.u.domain.vo.AccountInfoVo;
import com.u.domain.dto.UpdatePasswordDto;
/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
public interface IUserService extends IService<User> {
    /**
     * 用户登录方法
     * @param loginDto
     * @return 登录成功返回用户信息，否则返回 null
     */
    User login(LoginDto loginDto);

    /**
     * 用户注册方法
     * @param registerUserDto
     * @return 注册成功返回 true，失败返回 false
     */
    boolean register(RegisterDto registerUserDto);
    boolean updatePassword(UpdatePasswordDto updateDto);//修改密码
    /**
     * 重置密码
     * @param resetPasswordDto
     * @return
     */
    boolean resetPassword(ResetPasswordDto resetPasswordDto);

    /**
     * 获取账号信息
     * @return
     */
    AccountInfoVo getCurrentAccountInfo();

    /**
     * 通过手机号登录
     * @param phoneNumber
     * @return
     */
    User loginWithPhoneNumber(String phoneNumber);
}
