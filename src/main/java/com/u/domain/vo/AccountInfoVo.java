package com.u.domain.vo;

import lombok.Data;

//@Data
//public class AccountInfoVo {
//    private Integer userId;
//    private String username;
//    private int sex;
//    private String phone;
//}
@Data
public class AccountInfoVo {
    private Integer userId;
    private String username;
    private int sex;
    private String phone;
    private String message;

    // 可以添加toString方法以便更好的输出信息
    @Override
    public String toString() {
        if (message != null && !message.isEmpty()) {
            return "错误信息: " + message;
        }
        return "用户信息: {" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                '}';
    }
}