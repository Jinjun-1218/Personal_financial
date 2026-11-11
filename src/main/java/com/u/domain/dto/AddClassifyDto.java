package com.u.domain.dto;

import lombok.Data;

@Data
public class AddClassifyDto {
    private String type;            // 分类名称
    private int transactionType;    // 交易类型 (1: 收入, 0: 支出)
}
