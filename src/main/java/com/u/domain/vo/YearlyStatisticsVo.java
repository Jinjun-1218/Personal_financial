package com.u.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class YearlyStatisticsVo {
    private Integer year; // 年份
    private float totalIncome; // 总收入
    private float totalExpenditure; // 总支出
}
