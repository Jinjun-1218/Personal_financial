package com.u.controller;


import com.u.constant.MessageConstant;
import com.u.domain.dto.AddClassifyDto;
import com.u.domain.vo.ClassifyVo;
import com.u.result.Result;
import com.u.service.IClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 支出分类表 前端控制器
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@RestController
@RequestMapping("/classify")
@Api(tags = "分类接口")
public class ClassifyController {
    private static final Logger log = LoggerFactory.getLogger(ClassifyController.class);
    @Autowired
    private IClassifyService classifyService;


    // 查询所有分类
    @GetMapping("/all")
    @ApiOperation("查询所有分类")
    public Result<List<ClassifyVo>> getAllClassifies() {
        log.info("查询所有分类");
        // 查询所有分类
        List<ClassifyVo> classifyList = classifyService.getAllClassifies();

        if (classifyList != null && !classifyList.isEmpty()) {
            return Result.success(classifyList);
        } else {
            return Result.error( "未找到任何分类");
        }
    }
    @PostMapping("/add")
    @ApiOperation("新增分类")
    public Result<String> addClassify(@RequestBody AddClassifyDto addClassifyDto) {
        log.info("新增分类：{}, 交易类型：{}", addClassifyDto.getType(), addClassifyDto.getTransactionType());
        boolean result = classifyService.addClassify(addClassifyDto);
        if (result) {
            return Result.success(MessageConstant.INSERT_SUCCESS);
        } else {
            return Result.error(MessageConstant.INSERT_FAILED);
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation("根据分类名称删除分类")
    public Result<String> deleteClassify(@RequestParam String type) {
        log.info("删除分类，分类名称：{}", type);
        boolean result = classifyService.deleteClassifyByType(type);
        if (result) {
            return Result.success(MessageConstant.DELETE_SUCCESS);
        } else {
            return Result.error(MessageConstant.DELETE_FAILED);
        }
    }
}
