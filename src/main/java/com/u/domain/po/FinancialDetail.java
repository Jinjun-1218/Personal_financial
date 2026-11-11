package com.u.domain.po;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FinancialDetail {
    private double amount; // 金额
    private String transactionType; // 交易类型：收入/支出
    private String username; // 用户名
    private String date; // 交易日期
}
