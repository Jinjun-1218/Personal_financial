package com.u.domain.vo;

import lombok.Data;

import java.time.Month;

@Data
public class MonthStatisticsVo {
    private Month month;  // 当前月份
    private float incomeTotal;  // 该月的收入总额
    private float expenseTotal;  // 该月的支出总额
}
