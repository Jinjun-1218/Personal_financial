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
 * 群组表
 * </p>
 *
 * @author u
 * @since 2024-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("groups_info")
@ApiModel(value="Groups对象", description="群组表")
public class GroupsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键,群组的唯一标识符")
    @TableId(value = "group_id", type = IdType.AUTO)
    private Integer groupId;

    @ApiModelProperty(value = "群组名称")
    private String groupName;

    @ApiModelProperty(value = "管理员")
    private Integer administrator;

    @ApiModelProperty(value = "邀请码")
    private String invitationCode;


}
