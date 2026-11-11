package com.u.controller;


import com.u.constant.MessageConstant;
import com.u.domain.dto.FinancialBillDto;
import com.u.domain.po.FinancialBill;
import com.u.domain.vo.QueryBillVo;
import com.u.result.Result;
import com.u.service.IFinancialBillService;
import com.u.service.IGroupFinancialBillService;
import com.u.service.IGroupMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 账单表 前端控制器
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@RestController
@RequestMapping("/financial-bill")
@Api(tags = "账单管理")
public class FinancialBillController {
    private static final Logger log = LoggerFactory.getLogger(FinancialBillController.class);
    @Autowired
    private IFinancialBillService financialBillService;

    /**
     * 新增账单
     * @param billDto
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("新增账单")
    public Result<String> addFinancialBill(@RequestBody FinancialBillDto billDto) {
        log.info("新增账单：{}", billDto);
        boolean result = financialBillService.addFinancialBill(billDto);
        return result ? Result.success(MessageConstant.INSERT_SUCCESS) : Result.success(MessageConstant.INSERT_FAILED);
    }
    /**
     * 查询指定日期的账单
     * @param datetime 查询的日期
     * @return 查询结果列表
     */
    @GetMapping("/list")
    @ApiOperation("查询指定日期的账单列表")
    public Result<List<QueryBillVo>> queryFinancialBills(@RequestParam("datetime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate datetime) {
        // 执行查询
        List<QueryBillVo> result = financialBillService.queryFinancialBills(datetime);

        // 返回结果
        return Result.success(result);
    }

}
