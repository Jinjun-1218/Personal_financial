package com.u.service;

import com.u.domain.dto.AddClassifyDto;
import com.u.domain.po.Classify;
import com.baomidou.mybatisplus.extension.service.IService;
import com.u.domain.vo.ClassifyVo;

import java.util.List;

/**
 * <p>
 * 支出分类表 服务类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
public interface IClassifyService extends IService<Classify> {

    /**
     * 新增分类
     * @param addClassifyDto
     * @return
     */
    boolean addClassify(AddClassifyDto addClassifyDto);

    /**
     * 查询分类
     * @return
     */
    List<ClassifyVo> getAllClassifies();

    /**
     * 删除分类
     * @param type
     * @return
     */
    boolean deleteClassifyByType(String type);
}
