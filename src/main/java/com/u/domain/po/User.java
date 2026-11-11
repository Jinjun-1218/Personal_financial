package com.u.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@ApiModel(value="User对象", description="用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键,用户的唯一标识符")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @ApiModelProperty(value = "账号")
    private String accountName;

    @ApiModelProperty(value = "密码 初始密码为:123456")
    private String password;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "性别 默认值1:男 0:女")
    private Integer sex;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "选择微信登录")
    private String wechatlogin;

    @ApiModelProperty(value = "选择qq登录")
    private String qqlogin;


}
