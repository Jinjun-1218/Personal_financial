package com.u.domain.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {
    private String oldPassword;  // 旧密码
    private String newPassword;  // 新密码
    private String accountName;
}