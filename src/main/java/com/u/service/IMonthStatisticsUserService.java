package com.u.service;

import com.u.domain.po.MonthStatisticsUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.u.domain.vo.MonthStatisticsVo;
import com.u.domain.vo.YearlyStatisticsVo;
import com.u.result.Result;

import java.util.List;

/**
 * <p>
 * 用户月收支统计表 服务类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
public interface IMonthStatisticsUserService extends IService<MonthStatisticsUser> {

    /**
     * 月度统计
     * @param year
     * @return
     */
    Result<List<MonthStatisticsVo>> generateMonthlyStatistics(Integer year);

    /**
     * 年度统计
     * @return
     */
    Result<List<YearlyStatisticsVo>> generateYearlyStatistics();
}
