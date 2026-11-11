package com.u.service;

import com.u.domain.dto.FinancialBillDto;
import com.u.domain.po.FinancialBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.u.domain.vo.QueryBillVo;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 账单表 服务类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
public interface IFinancialBillService extends IService<FinancialBill> {

    /**
     * 新增账单
     * @param billDto
     * @return
     */
    boolean addFinancialBill(FinancialBillDto billDto);

    /**
     * 查询账单
     * @param date
     * @return
     */
    List<QueryBillVo> queryFinancialBills(LocalDate date);
}
