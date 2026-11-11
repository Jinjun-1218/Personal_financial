package com.u.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 群组成员表
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("group_member")
@ApiModel(value="GroupMember对象", description="群组成员表")
public class GroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键,群组成员表的唯一标识符")
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    @ApiModelProperty(value = "外键，用户id")
    private Integer userId;

    @ApiModelProperty(value = "外键，群组id")
    private Integer groupId;

    @ApiModelProperty(value = "加入群组时间")
    private LocalDateTime entergroupTime;


}
