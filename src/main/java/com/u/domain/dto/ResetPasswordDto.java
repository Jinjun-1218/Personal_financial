package com.u.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("新密码")
    private String newPassword;
}
