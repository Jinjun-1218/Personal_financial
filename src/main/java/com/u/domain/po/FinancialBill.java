package com.u.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 账单表
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("financial_bill")
@ApiModel(value="FinancialBill对象", description="账单表")
public class FinancialBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，账单表的唯一标识符")
    @TableId(value = "bill_id", type = IdType.AUTO)
    private Integer billId;

    @ApiModelProperty(value = "收入或支出 1表示收入 0表示支出")
    private Integer transactionType;

    @ApiModelProperty(value = "金额")
    private Float amount;

    @ApiModelProperty(value = "日期")
    private LocalDateTime datetime;

    @ApiModelProperty(value = "类别 ")
    private Integer classifyId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;
}
