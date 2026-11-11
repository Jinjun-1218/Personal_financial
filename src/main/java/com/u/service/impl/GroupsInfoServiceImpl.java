package com.u.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.u.context.BaseContext;
import com.u.domain.po.GroupMember;
import com.u.domain.po.GroupsInfo;
import com.u.domain.vo.GroupInfoVO;
import com.u.mapper.GroupMemberMapper;
import com.u.mapper.GroupsInfoMapper;
import com.u.service.IGroupsInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 群组表 服务实现类
 * </p>
 *
 * @author u
 * @since 2024-12-24
 */
import org.springframework.transaction.annotation.Transactional; // 添加事务注解
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class GroupsInfoServiceImpl extends ServiceImpl<GroupsInfoMapper, GroupsInfo> implements IGroupsInfoService {

    @Autowired
    private GroupsInfoMapper groupsInfoMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    public String generateInvitationCode() {
        // 使用UUID生成邀请码，并截取一部分
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 确保这个方法在一个事务中执行
    public void createGroup(String groupName, Integer adminId) {
        if (StringUtils.isEmpty(groupName) || adminId == null) {
            throw new IllegalArgumentException("群组名称和管理员ID不能为空");
        }

        // 创建群组对象
        GroupsInfo group = new GroupsInfo();
        group.setGroupName(groupName);
        group.setAdministrator(adminId);
        group.setInvitationCode(generateInvitationCode());

        // 保存到数据库
        this.save(group);

        // 获取群组ID
        Integer groupId = group.getGroupId();

        // 插入管理员作为群组成员
        GroupMember groupMember = new GroupMember();
        groupMember.setUserId(adminId); // 设置管理员ID
        groupMember.setGroupId(groupId); // 设置群组ID
        groupMember.setEntergroupTime(LocalDateTime.now()); // 设置加入群组时间为当前时间

        // 保存到群组成员表
        groupMemberMapper.insert(groupMember);
    }

    @Override
    public List<GroupsInfo> getGroupsByGroupIds(List<Integer> groupIds) {
        // 查询群组表，根据多个group_id查找群组名称
        return this.list(new QueryWrapper<GroupsInfo>().in("group_id", groupIds));
    }

    /**
     * 获取当前用户作为管理员的所有群组信息
     * @return List<GroupInfoVO> 群组名称和邀请代码的封装信息
     */
    @Override
    public List<GroupInfoVO> getAdministratorGroups() {
        // 获取当前登录用户的ID
        Integer userId = BaseContext.getCurrentId();

        // 使用 LambdaQueryWrapper 构造查询条件
        LambdaQueryWrapper<GroupsInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupsInfo::getAdministrator, userId); // 根据administrator字段进行筛选

        // 查询群组信息
        List<GroupsInfo> groupsInfoList = groupsInfoMapper.selectList(queryWrapper);

        // 如果没有找到任何群组，返回空列表
        if (groupsInfoList == null || groupsInfoList.isEmpty()) {
            return null;
        }

        return groupsInfoList.stream()
                .map(group -> {
                    GroupInfoVO groupInfoVO = new GroupInfoVO();
                    groupInfoVO.setGroupName(group.getGroupName());  // 仅设置群组名称
                    // 已移除设置邀请码的代码
                    //groupInfoVO.setInvitationCode(group.getInvitationCode());  // 设置邀请代码
                    return groupInfoVO;
                })
                .collect(Collectors.toList());
    }
}

