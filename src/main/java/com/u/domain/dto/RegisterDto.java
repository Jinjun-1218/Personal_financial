package com.u.domain.dto;

import lombok.Data;


@Data
public class RegisterDto {
    private String username; // 用户名
    private String password; // 加密后的密码
    private String accountName; //账号
    private String phone;   //电话
    // 必须添加setter方法供加密使用
    public void setPassword(String password) {
        this.password = password;
    }
}
