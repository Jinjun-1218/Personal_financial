package com.u.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.u.domain.dto.AddClassifyDto;
import com.u.domain.po.Classify;
import com.u.domain.vo.ClassifyVo;
import com.u.mapper.ClassifyMapper;
import com.u.service.IClassifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 支出分类表 服务实现类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@Service
public class ClassifyServiceImpl extends ServiceImpl<ClassifyMapper, Classify> implements IClassifyService {

    @Autowired
    private ClassifyMapper classifyMapper;

    @Override
    public boolean addClassify(AddClassifyDto addClassifyDto) {
        Classify classify = new Classify();
        classify.setType(addClassifyDto.getType());
        classify.setTransactionType(addClassifyDto.getTransactionType());
        return classifyMapper.insert(classify) > 0;  // MyBatis-Plus提供的插入方法
    }

    @Override
    public List<ClassifyVo> getAllClassifies() {
        // 使用 QueryWrapper 构建查询条件，获取所有分类
        QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("type", "transaction_type");  // 只查询类型和交易类型字段

        // 查询数据库，返回 Classify 对象列表
        List<Classify> classifies = classifyMapper.selectList(queryWrapper);

        // 将查询结果转换成 ClassifyVo 列表
        List<ClassifyVo> classifyVos = classifies.stream()
                .map(classify -> new ClassifyVo(classify.getType(), classify.getTransactionType()))
                .collect(Collectors.toList());

        return classifyVos;
    }

    @Override
    public boolean deleteClassifyByType(String type) {
        // 构造查询条件
        QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);  // 根据type字段进行条件匹配

        // 执行删除操作
        return classifyMapper.delete(queryWrapper) > 0;
    }
}
