package com.u.controller;


import com.u.domain.dto.GroupFinancialBillDto;
import com.u.domain.vo.GroupFinancialBillVo;
import com.u.result.Result;
import com.u.service.IGroupFinancialBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 群组账单表 前端控制器
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@RestController
@RequestMapping("/group-financial-bill")
@Api(tags = "群组账单")
public class GroupFinancialBillController {
    private static final Logger log = LoggerFactory.getLogger(GroupFinancialBillController.class);
    @Autowired
    private IGroupFinancialBillService groupFinancialBillService;

    /**
     * 群组账单汇总
     * @param groupFinancialBillDto
     * @return
     */
    @PostMapping("/get-financial-summary")
    @ApiOperation("群组账单汇总")
    public Result<GroupFinancialBillVo> getFinancialSummary(@RequestBody GroupFinancialBillDto groupFinancialBillDto) {
        log.info("群组账单汇总：{}", groupFinancialBillDto);
        return groupFinancialBillService.getFinancialSummary(groupFinancialBillDto.getGroupName(), groupFinancialBillDto.getYearMonth());
    }
}
