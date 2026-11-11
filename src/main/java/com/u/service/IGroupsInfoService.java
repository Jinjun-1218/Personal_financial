package com.u.service;

import com.u.domain.po.GroupsInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.u.domain.vo.GroupInfoVO;

import java.util.List;

/**
 * <p>
 * 群组表 服务类
 * </p>
 *
 * @author u
 * @since 2024-12-24
 */
public interface IGroupsInfoService extends IService<GroupsInfo> {

    /**
     * 新建群组
     * @param groupNames
     * @param userId
     */
    void createGroup(String groupName, Integer userId);


    /**
     * 根据群组id查询群组
     * @param groupIds
     * @return
     */
    List<GroupsInfo> getGroupsByGroupIds(List<Integer> groupIds);

    /**
     * 获取管理员群组
     * @return
     */
    List<GroupInfoVO> getAdministratorGroups();
}
