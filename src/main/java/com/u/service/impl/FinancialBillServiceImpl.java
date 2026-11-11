//package com.u.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.u.constant.MessageConstant;
//import com.u.context.BaseContext;
//import com.u.domain.dto.FinancialBillDto;
//import com.u.domain.po.Classify;
//import com.u.domain.po.FinancialBill;
//import com.u.domain.po.GroupFinancialBill;
//import com.u.domain.po.GroupMember;
//import com.u.domain.vo.QueryBillVo;
//import com.u.mapper.ClassifyMapper;
//import com.u.mapper.FinancialBillMapper;
//import com.u.domain.po.MonthStatisticsUser;
//import com.u.mapper.GroupMemberMapper;
//import com.u.service.IFinancialBillService;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.u.service.IGroupFinancialBillService;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * <p>
// * 账单表 服务实现类
// * </p>
// *
// * @author u
// * @since 2024-12-05
// */
//import org.springframework.transaction.annotation.Transactional; // 添加事务注解
//@Service
//public class FinancialBillServiceImpl extends ServiceImpl<FinancialBillMapper, FinancialBill> implements IFinancialBillService {
//
//    @Autowired
//    private FinancialBillMapper financialBillMapper;
//    @Autowired
//    private ClassifyMapper classifyMapper;
//    @Autowired
//    private GroupMemberMapper groupMemberMapper;
//    @Autowired
//    private IGroupFinancialBillService groupFinancialBillService; // 注入 IGroupFinancialBillService
//    @Override
//    public boolean addFinancialBill(FinancialBillDto billDto) {
//        FinancialBill financialBill = new FinancialBill();
//
//        //通过类别名称获取类别ID
//        QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("type", billDto.getType());
//        Classify classify = classifyMapper.selectOne(queryWrapper);
//
//        if (classify != null) {
//            Integer classifyId = classify.getClassifyId();
//            // 处理 classifyId
//            BeanUtils.copyProperties(billDto, financialBill);
//            financialBill.setClassifyId(classifyId);
//            Integer currentId = BaseContext.getCurrentId();
//            financialBill.setUserId(currentId);
//            // 保存个人账单
//            boolean personalResult = save(financialBill);
//            // 更新月统计表
//            updateMonthlyStatistics(financialBill);
//
//            // 如果 remark 为 "1"，需要插入群组账单
//            if ("1".equals(billDto.getRemark())) {
//                boolean groupResult = addGroupFinancialBillForUser(billDto, currentId, classifyId);
//                return personalResult && groupResult;
//            }
//            return personalResult;
//        }else {
//                // 处理未找到分类的情况
//                return false;
//            }
//        }
//    /**
//     * 处理群组账单插入逻辑
//     *
//     * @param billDto     账单数据
//     * @param userId      当前用户 ID
//     * @param classifyId  类别 ID
//     * @return 插入结果
//     */
//    private void updateMonthlyStatistics(FinancialBill financialBill) {
//        Integer userId = financialBill.getUserId();
//        LocalDateTime datetime = financialBill.getDatetime();
//        float amount = financialBill.getAmount();
//        int transactionType = financialBill.getTransactionType();
//
//        // 查询用户的月统计记录
//        QueryWrapper<MonthStatisticsUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", userId);
//        queryWrapper.eq("year", datetime.getYear());
//        queryWrapper.eq("month", datetime.getMonthValue());
//
//        MonthStatisticsUser statistics = monthStatisticsUserMapper.selectOne(queryWrapper);
//        if (statistics == null) {
//            // 如果没有记录，创建新的统计记录
//            statistics = new MonthStatisticsUser();
//            statistics.setUserId(userId);
//            statistics.setYear(datetime.getYear());
//            statistics.setMonth(datetime.getMonthValue());
//            statistics.setIncomeTotal(transactionType == 1 ? amount : 0);
//            statistics.setExpenseTotal(transactionType == 0 ? amount : 0);
//            monthStatisticsUserMapper.insert(statistics);
//        } else {
//            // 更新现有的统计记录
//            if (transactionType == 1) {
//                statistics.setIncomeTotal(statistics.getIncomeTotal() + amount);
//            } else if (transactionType == 0) {
//                statistics.setExpenseTotal(statistics.getExpenseTotal() + amount);
//            }
//            monthStatisticsUserMapper.updateById(statistics);
//        }
//    }
//    private boolean addGroupFinancialBillForUser (FinancialBillDto billDto,int userId, int classifyId){
//        // 使用 MyBatis-Plus 查询当前用户加入的群组 ID 列表
//        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(GroupMember::getUserId, userId).select(GroupMember::getGroupId);
//
//        // 获取群组 ID 列表并转换为 Integer 类型
//        List<Integer> groupIds = groupMemberMapper.selectObjs(queryWrapper) //查询返回的是 List<Object> 类型
//                .stream()
//                .map(obj -> (Integer) obj) // 类型安全转换
//                .collect(Collectors.toList());
//
//        if (groupIds != null && !groupIds.isEmpty()) {
//            List<GroupFinancialBill> groupBills = new ArrayList<>();
//
//            // 为每个群组构建群组账单记录
//            for (Integer groupId : groupIds) {
//                GroupFinancialBill groupBill = new GroupFinancialBill();
//                groupBill.setTransactionType(billDto.getTransactionType());
//                groupBill.setUserId(userId);
//                groupBill.setAmount(billDto.getAmount());
//                groupBill.setDatetime(billDto.getDatetime());
//                groupBill.setGroupId(groupId);
//                groupBill.setClassifyId(classifyId);
//                groupBills.add(groupBill);
//            }
//
//            // 使用 MyBatis-Plus 批量插入群组账单
//            return groupFinancialBillService.saveBatch(groupBills);
//        }
//
//        // 如果用户未加入任何群组，认为成功
//        return true;
//    }
//    @Override
//    public List<QueryBillVo> queryFinancialBills(LocalDate datetime) {
//        // 1. 构造查询条件
//        QueryWrapper<FinancialBill> queryWrapper = new QueryWrapper<>();
//
//        // 获取当前用户的 userId
//        Integer userId = BaseContext.getCurrentId();
//
//        if (userId == null) {
//            throw new RuntimeException(MessageConstant.NOT_LOGIN);
//        }
//
//        // 2. 查询时间范围
//        LocalDateTime startOfDay = datetime.atStartOfDay();
//        queryWrapper.ge("datetime", startOfDay);
//
//        LocalDateTime endOfDay = datetime.atTime(23, 59, 59);
//        queryWrapper.le("datetime", endOfDay);
//
//        // 3. 根据当前用户的 userId 查询
//        queryWrapper.eq("user_id", userId);
//
//        // 4. 查询账单记录
//        List<FinancialBill> financialBills = financialBillMapper.selectList(queryWrapper);
//
//        // 5. 将查询到的 FinancialBill 转换为 QueryBillVo
//        List<QueryBillVo> result = financialBills.stream().map(financialBill -> {
//            QueryBillVo vo = new QueryBillVo();
//            vo.setTransactionType(financialBill.getTransactionType());
//            vo.setAmount(financialBill.getAmount());
//
//            // 6. 查询分类名称
//            Integer classifyId = financialBill.getClassifyId();
//            Classify classify = classifyMapper.selectById(classifyId);  // classifyMapper查询分类
//
//            if (classify != null) {
//                vo.setType(classify.getType());  // 将分类名称设置到 VO 中
//            }
//
//            return vo;
//        }).collect(Collectors.toList());
//
//        // 7. 返回封装后的结果
//        return result;
//    }
//}
//
//
//
package com.u.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.u.constant.MessageConstant;
import com.u.context.BaseContext;
import com.u.domain.dto.FinancialBillDto;
import com.u.domain.po.Classify;
import com.u.domain.po.FinancialBill;
import com.u.domain.po.GroupFinancialBill;
import com.u.domain.po.GroupMember;
import com.u.domain.vo.QueryBillVo;
import com.u.mapper.ClassifyMapper;
import com.u.mapper.FinancialBillMapper;
import com.u.mapper.GroupMemberMapper;
import com.u.service.IFinancialBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.u.service.IGroupFinancialBillService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 账单表 服务实现类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Service
public class FinancialBillServiceImpl extends ServiceImpl<FinancialBillMapper, FinancialBill> implements IFinancialBillService {

    @Autowired
    private FinancialBillMapper financialBillMapper;
    @Autowired
    private ClassifyMapper classifyMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private IGroupFinancialBillService groupFinancialBillService; // 注入 IGroupFinancialBillService
    @Override
    public boolean addFinancialBill(FinancialBillDto billDto) {
        FinancialBill financialBill = new FinancialBill();

        //通过类别名称获取类别ID
        QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", billDto.getType());
        Classify classify = classifyMapper.selectOne(queryWrapper);

        if (classify != null) {
            Integer classifyId = classify.getClassifyId();
            // 处理 classifyId
            BeanUtils.copyProperties(billDto, financialBill);
            financialBill.setClassifyId(classifyId);
            Integer currentId = BaseContext.getCurrentId();
            financialBill.setUserId(currentId);
            // 保存个人账单
            boolean personalResult = save(financialBill);

            // 如果 remark 为 "1"，需要插入群组账单
            if ("1".equals(billDto.getRemark())) {
                boolean groupResult = addGroupFinancialBillForUser(billDto, currentId, classifyId);
                return personalResult && groupResult;
            }
            return personalResult;
        }else {
            // 处理未找到分类的情况
            return false;
        }
    }
    /**
     * 处理群组账单插入逻辑
     *
     * @param billDto     账单数据
     * @param userId      当前用户 ID
     * @param classifyId  类别 ID
     * @return 插入结果
     */
    private boolean addGroupFinancialBillForUser (FinancialBillDto billDto,int userId, int classifyId){
        // 使用 MyBatis-Plus 查询当前用户加入的群组 ID 列表
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getUserId, userId).select(GroupMember::getGroupId);

        // 获取群组 ID 列表并转换为 Integer 类型
        List<Integer> groupIds = groupMemberMapper.selectObjs(queryWrapper) //查询返回的是 List<Object> 类型
                .stream()
                .map(obj -> (Integer) obj) // 类型安全转换
                .collect(Collectors.toList());

        if (groupIds != null && !groupIds.isEmpty()) {
            List<GroupFinancialBill> groupBills = new ArrayList<>();

            // 为每个群组构建群组账单记录
            for (Integer groupId : groupIds) {
                GroupFinancialBill groupBill = new GroupFinancialBill();
                groupBill.setTransactionType(billDto.getTransactionType());
                groupBill.setUserId(userId);
                groupBill.setAmount(billDto.getAmount());
                groupBill.setDatetime(billDto.getDatetime());
                groupBill.setGroupId(groupId);
                groupBill.setClassifyId(classifyId);
                groupBills.add(groupBill);
            }

            // 使用 MyBatis-Plus 批量插入群组账单
            return groupFinancialBillService.saveBatch(groupBills);
        }

        // 如果用户未加入任何群组，认为成功
        return true;
    }
    @Override
    public List<QueryBillVo> queryFinancialBills(LocalDate datetime) {
        // 1. 构造查询条件
        QueryWrapper<FinancialBill> queryWrapper = new QueryWrapper<>();

        // 获取当前用户的 userId
        Integer userId = BaseContext.getCurrentId();

        if (userId == null) {
            throw new RuntimeException(MessageConstant.NOT_LOGIN);
        }

        // 2. 查询时间范围
        LocalDateTime startOfDay = datetime.atStartOfDay();
        queryWrapper.ge("datetime", startOfDay);

        LocalDateTime endOfDay = datetime.atTime(23, 59, 59);
        queryWrapper.le("datetime", endOfDay);

        // 3. 根据当前用户的 userId 查询
        queryWrapper.eq("user_id", userId);

        // 4. 查询账单记录
        List<FinancialBill> financialBills = financialBillMapper.selectList(queryWrapper);

        // 5. 将查询到的 FinancialBill 转换为 QueryBillVo
        List<QueryBillVo> result = financialBills.stream().map(financialBill -> {
            QueryBillVo vo = new QueryBillVo();
            vo.setTransactionType(financialBill.getTransactionType());
            vo.setAmount(financialBill.getAmount());

            // 6. 查询分类名称
            Integer classifyId = financialBill.getClassifyId();
            Classify classify = classifyMapper.selectById(classifyId);  // classifyMapper查询分类

            if (classify != null) {
                vo.setType(classify.getType());  // 将分类名称设置到 VO 中
            }

            return vo;
        }).collect(Collectors.toList());

        // 7. 返回封装后的结果
        return result;
    }
}




