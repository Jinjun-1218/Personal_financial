package com.u.domain.vo;

import lombok.Data;

@Data
public class QueryBillVo {
    private int transactionType;    //交易类型
    private String type;    //类型
    private float amount;   //金额
}
