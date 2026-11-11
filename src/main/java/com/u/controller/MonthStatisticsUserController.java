package com.u.controller;


import com.u.domain.vo.MonthStatisticsVo;
import com.u.domain.vo.YearlyStatisticsVo;
import com.u.result.Result;
import com.u.service.IMonthStatisticsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户月收支统计表 前端控制器
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Api(tags = "用户月度统计接口")
@RestController
@RequestMapping("/month-statistics-user")
public class MonthStatisticsUserController {
    @Autowired
    private IMonthStatisticsUserService monthStatisticsService;

    /**
     * 月度统计
     * @param year
     * @return
     */
    @PostMapping("/generate/month")
    @ApiOperation("月度统计")
    public Result<List<MonthStatisticsVo>> generateMonthlyStatistics(@RequestParam Integer year) {
        return monthStatisticsService.generateMonthlyStatistics(year);
    }

    /**
     * 年度统计
     * @return 年度收支统计
     */
    @PostMapping("/generate/years")
    @ApiOperation("年度统计")
    public Result<List<YearlyStatisticsVo>> generateYearlyStatistics() {
        return monthStatisticsService.generateYearlyStatistics();
    }
}
