package com.u.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.u.constant.MessageConstant;
import com.u.domain.dto.GroupFinancialBillDto;
import com.u.domain.po.FinancialDetail;
import com.u.domain.po.GroupFinancialBill;
import com.u.domain.po.GroupsInfo;
import com.u.domain.po.User;
import com.u.domain.vo.GroupFinancialBillVo;
import com.u.mapper.GroupFinancialBillMapper;
import com.u.mapper.GroupsInfoMapper;
import com.u.mapper.UserMapper;
import com.u.result.Result;
import com.u.service.IGroupFinancialBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 群组账单表 服务实现类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Service
public class GroupFinancialBillServiceImpl extends ServiceImpl<GroupFinancialBillMapper, GroupFinancialBill> implements IGroupFinancialBillService {

    @Autowired
    private GroupFinancialBillMapper groupFinancialBillMapper;
    @Autowired
    private UserMapper userMapper; // 注入 UserMapper
    @Autowired
    private GroupsInfoMapper groupMapper; // 注入 GroupMapper

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Result<GroupFinancialBillVo> getFinancialSummary(String groupName, LocalDate yearMonth) {
        GroupFinancialBillVo groupFinancialBillVo = new GroupFinancialBillVo();

        // 格式化 LocalDate 为 yyyy-MM 格式
        //String formattedYearMonth = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 根据群组名查询群组 ID
        LambdaQueryWrapper<GroupsInfo> groupQueryWrapper = new LambdaQueryWrapper<>();
        groupQueryWrapper.eq(GroupsInfo::getGroupName, groupName);
        GroupsInfo group = groupMapper.selectOne(groupQueryWrapper);

        if (group == null) {
            return Result.error(MessageConstant.GROUP_NOT_EXIST);
        }
        Integer groupId = group.getGroupId(); // 获取群组 ID

        // 根据 groupId 和年月查询相关的账单
        LambdaQueryWrapper<GroupFinancialBill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupFinancialBill::getGroupId, groupId)
                .ge(GroupFinancialBill::getDatetime, yearMonth.withDayOfMonth(1).atStartOfDay())
                .le(GroupFinancialBill::getDatetime, yearMonth.withDayOfMonth(yearMonth.lengthOfMonth()).atTime(23, 59, 59));

        // 根据年月匹配账单日期
        List<GroupFinancialBill> financialBills = groupFinancialBillMapper.selectList(queryWrapper);
        // 计算收入和支出
        float income = 0;
        float expense = 0;
        List<FinancialDetail> incomeDetails = new ArrayList<>();
        List<FinancialDetail> expenseDetails = new ArrayList<>();

        for (GroupFinancialBill bill : financialBills) {
            FinancialDetail financialDetail = new FinancialDetail();
            financialDetail.setAmount(bill.getAmount());
            // 获取交易日期并格式化为 yyyy-MM-dd
            LocalDate transactionDate = bill.getDatetime().toLocalDate();  // 将 LocalDateTime 转换为 LocalDate
            String formattedDate = transactionDate.format(DATE_FORMATTER);  // 格式化日期
            financialDetail.setDate(formattedDate);  // 设置格式化后的日期
            // 根据交易类型判断收入和支出
            if (bill.getTransactionType() == 1) {  // 收入
                income += bill.getAmount();
                financialDetail.setTransactionType("收入");
                // 获取用户名
                String username = getUsernameByUserId(bill.getUserId());
                financialDetail.setUsername(username);
                incomeDetails.add(financialDetail);
            } else if (bill.getTransactionType() == 0) {  // 支出
                expense += bill.getAmount();
                financialDetail.setTransactionType("支出");
                // 获取用户名
                String username = getUsernameByUserId(bill.getUserId());
                financialDetail.setUsername(username);
                expenseDetails.add(financialDetail);
            }
        }

        // 设置返回的Vo对象
        groupFinancialBillVo.setTotalIncome(income);
        groupFinancialBillVo.setTotalExpense(expense);
        groupFinancialBillVo.setIncomeDetails(incomeDetails);
        groupFinancialBillVo.setExpenseDetails(expenseDetails);

        // 返回结果
        return Result.success(groupFinancialBillVo);
    }

    // 根据userId查询用户名
    private String getUsernameByUserId(Integer userId) {
        // 查询用户表，获取用户名
        User user = userMapper.selectById(userId);
        return user.getUsername();
    }
}

