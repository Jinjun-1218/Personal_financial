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
 * 群组账单表
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("group_financial_bill")
@ApiModel(value="GroupFinancialBill对象", description="群组账单表")
public class GroupFinancialBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，群组账单表的唯一标识符")
    @TableId(value = "groupbill_id", type = IdType.AUTO)
    private Integer groupbillId;

    @ApiModelProperty(value = "收入或支出 1表示收入 0表示支出")
    private Integer transactionType;

    @ApiModelProperty(value = "外键，用户id")
    private Integer userId;

    @ApiModelProperty(value = "金额")
    private Float amount;

    @ApiModelProperty(value = "日期")
    private LocalDateTime datetime;

    @ApiModelProperty(value = "外键，群组id")
    private Integer groupId;

    @ApiModelProperty(value = "类型")
    private Integer classifyId;


}
