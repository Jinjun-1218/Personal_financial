package com.u.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.u.constant.MessageConstant;
import com.u.context.BaseContext;
import com.u.domain.po.FinancialBill;
import com.u.domain.po.MonthStatisticsUser;
import com.u.domain.vo.MonthStatisticsVo;
import com.u.domain.vo.YearlyStatisticsVo;
import com.u.mapper.FinancialBillMapper;
import com.u.mapper.MonthStatisticsUserMapper;
import com.u.result.Result;
import com.u.service.IMonthStatisticsUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户月收支统计表 服务实现类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Service
public class MonthStatisticsUserServiceImpl extends ServiceImpl<MonthStatisticsUserMapper, MonthStatisticsUser> implements IMonthStatisticsUserService {

    @Autowired
    private FinancialBillMapper financialBillMapper;
    @Autowired
    private MonthStatisticsUserMapper monthStatisticsUserMapper;

    @Override
    public Result<List<MonthStatisticsVo>> generateMonthlyStatistics(Integer year) {
        // 获取当前用户的 ID
        Integer userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error(MessageConstant.NOT_LOGIN);
        }

        // 查询指定年份和用户的所有账单数据
        QueryWrapper<FinancialBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.like("datetime", year + "-"); // 按年份查询
        List<FinancialBill> bills = financialBillMapper.selectList(queryWrapper);

        if (bills.isEmpty()) {
            return Result.error("没有找到该用户的账单数据");
        }

        // 用 Map 来存储每月的收支数据
        Map<Month, float[]> monthlyStats = new HashMap<>();

        // 初始化每月的收支
        //Month.values() 方法：
        //Month.values() 是枚举类的静态方法，返回一个包含该枚举类所有枚举常量的数组。
        //对于 Month 枚举，Month.values() 返回的是一个包含 12 个元素的数组，每个元素是一个 Month 枚举实例，分别对应一年中的 12 个月份。
        for (Month month : Month.values()) {
            monthlyStats.put(month, new float[]{0.0f, 0.0f});  // index 0: 收入总额, index 1: 支出总额
        }

        // 按月份和类型统计收支
        for (FinancialBill bill : bills) {
            Month month = bill.getDatetime().getMonth();
            float amount = bill.getAmount();

            // 收入
            if (bill.getTransactionType() == 1) {
                monthlyStats.get(month)[0] += amount;
            }
            // 支出
            else if (bill.getTransactionType() == 0) {
                monthlyStats.get(month)[1] += amount;
            }
        }

        // 构建每月的统计数据
        List<MonthStatisticsVo> result = new ArrayList<>();
        for (Map.Entry<Month, float[]> entry : monthlyStats.entrySet()) {   //遍历所有键值对
            // 获取月份
            Month month = entry.getKey();

            // 获取收入和支出数据
            float[] stats = entry.getValue();
            float incomeTotal = stats[0];  // 收入总额
            float expenseTotal = stats[1]; // 支出总额

            MonthStatisticsVo monthStatisticsVo = new MonthStatisticsVo();
            monthStatisticsVo.setMonth(month);
            monthStatisticsVo.setIncomeTotal(incomeTotal);
            monthStatisticsVo.setExpenseTotal(expenseTotal);

            result.add(monthStatisticsVo);

        }

        return Result.success(result);  // 返回每月收支统计结果
    }

    @Override
    public Result<List<YearlyStatisticsVo>> generateYearlyStatistics() {
        // 获取当前用户的 ID
        Integer userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error(MessageConstant.NOT_LOGIN);
        }

        // 查询该用户的所有账单数据（不限年份）
        List<FinancialBill> bills = financialBillMapper.selectList(new QueryWrapper<FinancialBill>().eq("user_id", userId));

        if (bills.isEmpty()) {
            return Result.error("没有找到该用户的账单数据");
        }

        // 用 Map 来存储每年的收支数据
        Map<Integer, float[]> yearlyStats = new HashMap<>();

        // 按年份和类型统计收入和支出
        for (FinancialBill bill : bills) {
            Integer year = bill.getDatetime().getYear();  // 获取年份
            float amount = bill.getAmount(); // 获取金额

            // 如果该年份没有初始化，则初始化
            if (!yearlyStats.containsKey(year)) {
                yearlyStats.put(year, new float[]{0.0f, 0.0f});  // index 0: 收入总额, index 1: 支出总额
            }

            // 根据交易类型更新收入或支出
            if (bill.getTransactionType() == 1) {  // 收入
                yearlyStats.get(year)[0] += amount;
            } else if (bill.getTransactionType() == 0) {  // 支出
                yearlyStats.get(year)[1] += amount;
            }
        }

        // 构建每年的统计数据
        List<YearlyStatisticsVo> result = new ArrayList<>();
        for (Map.Entry<Integer, float[]> entry : yearlyStats.entrySet()) {
            // 获取年份
            Integer year = entry.getKey();

            // 获取收入和支出数据
            float[] stats = entry.getValue();
            float incomeTotal = stats[0];  // 收入总额
            float expenseTotal = stats[1]; // 支出总额

            // 构建 YearlyStatisticsVo 并赋值
            YearlyStatisticsVo yearlyStatisticsVo = new YearlyStatisticsVo();
            yearlyStatisticsVo.setYear(year);
            yearlyStatisticsVo.setTotalIncome(incomeTotal);
            yearlyStatisticsVo.setTotalExpenditure(expenseTotal);

            result.add(yearlyStatisticsVo);
        }

        return Result.success(result);  // 返回年收支统计结果
    }
}


