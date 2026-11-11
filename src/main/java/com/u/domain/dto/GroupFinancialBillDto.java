package com.u.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GroupFinancialBillDto {
    private String groupName;  // 群组名称
    private LocalDate yearMonth; // 年月，格式：yyyy-MM
}
