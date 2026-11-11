package com.u.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FinancialBillDto {
    private LocalDateTime datetime; //日期
    private int transactionType;    //交易类型
    private String type;    //类型
    private float amount;   //金额
    private String remark;  //是否加入群组
}
