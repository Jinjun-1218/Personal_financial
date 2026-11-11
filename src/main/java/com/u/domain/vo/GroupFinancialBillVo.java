package com.u.domain.vo;

import com.u.domain.po.FinancialDetail;
import lombok.Data;

import java.util.List;

@Data
public class GroupFinancialBillVo {
    private double totalIncome;  // 总收入
    private double totalExpense; // 总支出
    private List<FinancialDetail> incomeDetails; // 收入明细
    private List<FinancialDetail> expenseDetails; // 支出明细
}
