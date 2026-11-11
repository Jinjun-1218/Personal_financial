package com.u.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.u.constant.MessageConstant;
import com.u.context.BaseContext;
import com.u.domain.dto.LoginDto;
import com.u.domain.dto.RegisterDto;
import com.u.domain.dto.ResetPasswordDto;
import com.u.domain.po.User;
import com.u.domain.vo.AccountInfoVo;
import com.u.mapper.UserMapper;
import com.u.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
import com.u.domain.dto.UpdatePasswordDto;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    //    @Override
//    public User login(LoginDto loginDto) {
//        // 构造查询条件
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("account_name", loginDto.getAccountName())
//                .eq("password", loginDto.getPassword());
//
//        // 执行查询
//        return userMapper.selectOne(queryWrapper);
//    }
    @Override
    public User login(LoginDto loginDto) {
        // 对用户输入的原始密码进行MD5加密
        String encryptedInput = DigestUtils.md5DigestAsHex(
                loginDto.getPassword().getBytes(StandardCharsets.UTF_8)
        );

        // 根据账号查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccountName, loginDto.getAccountName());

        User dbUser = this.getOne(queryWrapper);

        // 验证用户存在且密码匹配（直接比较加密后的值）
        return (dbUser != null && encryptedInput.equals(dbUser.getPassword()))
                ? dbUser
                : null;
    }

    //    @Override
//    public boolean register(RegisterDto registerUserDto) {
//        // 1. 校验用户名是否已存在
//        if (isUsernameExist(registerUserDto.getUsername())) {
//            throw new RuntimeException(MessageConstant.USER_EXIST);
//        }
//
//        // 2. 创建 User 对象
//        User newUser = new User();
//
//        // 3. 使用 BeanUtils 拷贝属性
//        BeanUtils.copyProperties(registerUserDto, newUser);
//
//        return this.save(newUser);  // 调用 MyBatis-Plus 的 save 方法保存用户
//    }
    @Override
    public boolean register(RegisterDto registerUserDto) {
        // 1. 校验用户名是否已存在
        if (isUsernameExist(registerUserDto.getUsername())) {
            throw new RuntimeException(MessageConstant.USER_EXIST);
        }

        // 2. 创建 User 对象
        User newUser = new User();

        // 3. 密码加密处理（添加的代码）
        String encryptedPassword = DigestUtils.md5DigestAsHex(
                registerUserDto.getPassword().getBytes(StandardCharsets.UTF_8)
        );
        registerUserDto.setPassword(encryptedPassword);

        // 4. 使用 BeanUtils 拷贝属性
        BeanUtils.copyProperties(registerUserDto, newUser);

        return this.save(newUser);
    }

    @Override
    public boolean resetPassword(ResetPasswordDto resetPasswordDto) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, resetPasswordDto.getPhone());
        User user = userMapper.selectOne(queryWrapper);
        String encryptedPwd = DigestUtils.md5DigestAsHex(
                resetPasswordDto.getNewPassword().getBytes(StandardCharsets.UTF_8)
        );
        user.setPassword(encryptedPwd);
        if (user == null) {
            return false;
        }

        // 更新密码（对密码进行 MD5 加密存储）
        //String encryptedPassword = DigestUtils.md5DigestAsHex(resetPasswordDto.getNewPassword().getBytes());
        user.setPassword(resetPasswordDto.getNewPassword());

        int updateResult = userMapper.updateById(user);
        return updateResult > 0;
    }

    //    @Override
//    public AccountInfoVo getCurrentAccountInfo() {
//        //获取当前用户的 ID
//        Integer userId = BaseContext.getCurrentId();
//
//        // 使用 MyBatis-Plus 查询用户信息
//        User user = userMapper.selectById(userId);
//
//        // 如果用户信息存在，封装成 AccountInfoVo 返回
//        if (user != null) {
//            AccountInfoVo accountInfoVo = new AccountInfoVo();
//            accountInfoVo.setUserId(user.getUserId());
//            accountInfoVo.setUsername(user.getUsername());
//            accountInfoVo.setSex(user.getSex());
//            accountInfoVo.setPhone(user.getPhone());
//            return accountInfoVo;
//        }
//        return null; // 如果没有找到用户，返回 null
//    }
    @Override
    public AccountInfoVo getCurrentAccountInfo() {
        //获取当前用户的 ID
        Integer userId = BaseContext.getCurrentId();

        // 创建返回对象
        AccountInfoVo accountInfoVo = new AccountInfoVo();

        // 如果userId为null，说明缺少token参数
        if (userId == null) {
            accountInfoVo.setMessage("缺少token参数");
            System.out.println("获取用户信息失败: " + accountInfoVo.getMessage()); // 添加输出
            return accountInfoVo;
        }

        // 使用 MyBatis-Plus 查询用户信息
        User user = userMapper.selectById(userId);

        // 如果用户信息存在，封装成 AccountInfoVo 返回
        if (user != null) {
            accountInfoVo.setUserId(user.getUserId());
            accountInfoVo.setUsername(user.getUsername());
            accountInfoVo.setSex(user.getSex());
            accountInfoVo.setPhone(user.getPhone());
            System.out.println("成功获取用户信息: " + user.getUsername()); // 添加输出
            return accountInfoVo;
        }

        // 如果用户不存在，返回带有错误信息的AccountInfoVo
        accountInfoVo.setMessage("用户不存在");
        System.out.println("获取用户信息失败: " + accountInfoVo.getMessage()); // 添加输出
        return accountInfoVo;
    }
    //修改密码
    @Override
    public boolean updatePassword(UpdatePasswordDto updateDto) {
        // 获取当前用户ID
        Integer userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        if (updateDto.getAccountName() != null) {
            throw new RuntimeException("账号名称不可修改");
        }
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        String encryptedOld = DigestUtils.md5DigestAsHex(
                updateDto.getOldPassword().getBytes(StandardCharsets.UTF_8)
        );
        if (!user.getPassword().equals(encryptedOld)) {
            throw new RuntimeException("旧密码不正确");
        }

        // 加密新密码并更新
        String encryptedNew = DigestUtils.md5DigestAsHex(
                updateDto.getNewPassword().getBytes(StandardCharsets.UTF_8)
        );
        user.setPassword(encryptedNew);

        return userMapper.updateById(user) > 0;
    }

    @Override
    public User loginWithPhoneNumber(String phoneNumber) {
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phoneNumber);  // 条件：手机号等于传入的手机号

        // 执行查询，返回符合条件的用户
        return userMapper.selectOne(queryWrapper);  // 查询单个用户
    }
    /**
     * 校验用户名是否已存在
     */
    private boolean isUsernameExist(String username) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("username", username)) > 0;
    }
}


