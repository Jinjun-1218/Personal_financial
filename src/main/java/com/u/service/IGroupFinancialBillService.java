package com.u.service;

import com.u.domain.po.GroupFinancialBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.u.domain.vo.GroupFinancialBillVo;
import com.u.result.Result;

import java.time.LocalDate;

/**
 * <p>
 * 群组账单表 服务类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
public interface IGroupFinancialBillService extends IService<GroupFinancialBill> {

    /**
     * 群组账单
     * @param groupName
     * @param yearMonth
     * @return
     */
    Result<GroupFinancialBillVo> getFinancialSummary(String groupName, LocalDate yearMonth);
}
