//package com.u.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.u.constant.MessageConstant;
//import com.u.context.BaseContext;
//import com.u.domain.po.GroupMember;
//import com.u.domain.po.GroupsInfo;
//import com.u.domain.po.User;
//import com.u.domain.vo.UserVo;
//import com.u.mapper.GroupMemberMapper;
//import com.u.mapper.GroupsInfoMapper;
//import com.u.mapper.UserMapper;
//import com.u.service.IGroupMemberService;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * <p>
// * 群组成员表 服务实现类
// * </p>
// *
// * @author u
// * @since 2024-12-05
// */
//@Service
//public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements IGroupMemberService {
//
//    @Autowired
//    private GroupsInfoMapper groupMapper;  // 用于查询群组
//    @Autowired
//    private GroupMemberMapper groupMemberMapper;  // 用于插入群组成员记录
//    @Autowired
//    private UserMapper userMapper;
//
//    @Override
//    public boolean joinGroup(String inviteCode, Integer userId) {
//        // 调用封装好的方法获取群组ID
//        Integer groupId = getGroupIdByInviteCode(inviteCode);
//
//        if (groupId == null) {
//            return false;  // 如果没有找到群组，返回false
//        }
//
//        // 创建群组成员记录并插入到数据库
//        GroupMember groupMember = new GroupMember();
//        groupMember.setUserId(userId);
//        groupMember.setGroupId(groupId);
//        groupMember.setEntergroupTime(LocalDateTime.now());  // 设置加入群组的时间
//
//        // 插入群组成员
//        int rowsAffected = groupMemberMapper.insert(groupMember);
//        return rowsAffected > 0;  // 如果插入成功，返回true
//    }
//
//    @Override
//    public List<GroupMember> getGroupsByUserId() {
//        Integer userId = BaseContext.getCurrentId();
//        // 查询该用户加入的所有群组的group_id
//        return this.baseMapper.selectList(new QueryWrapper<GroupMember>().eq("user_id", userId));
//    }
//
//    @Override
//    public List<UserVo> getUsersByGroupName(String groupName) {
//        // 1. 根据群组名查询群组ID
////        GroupsInfo group = groupMapper.selectOne(new QueryWrapper<GroupsInfo>().eq("group_name", groupName));
////        if (group == null) {
////            throw new RuntimeException(MessageConstant.GROUP_NOT_EXIST);
////        }
////        Integer groupId = group.getGroupId();
////
////        // 2. 根据群组ID查询群组成员表中的用户ID
////        List<GroupMember> groupMembers = groupMemberMapper.selectList(new QueryWrapper<GroupMember>().eq("group_id", groupId));
//        // 使用Lambda表达式防止SQL注入
//        GroupsInfo group = groupMapper.selectOne(new QueryWrapper<GroupsInfo>()
//                .lambda()
//                .eq(GroupsInfo::getGroupName, groupName));
//
//        if (group == null) {
//            throw new RuntimeException(MessageConstant.GROUP_NOT_EXIST);
//        }
//
//        List<GroupMember> groupMembers = groupMemberMapper.selectList(new QueryWrapper<GroupMember>()
//                .lambda()
//                .eq(GroupMember::getGroupId, group.getGroupId()));
//        if (groupMembers.isEmpty()) {
//            throw new RuntimeException(MessageConstant.USER_NOT_EXIST);
//        }
//
//        // 3. 根据用户ID查询用户表并返回用户ID和用户名
//        List<Integer> userIds = groupMembers.stream().map(GroupMember::getUserId).collect(Collectors.toList());
//        List<User> users = userMapper.selectList(new QueryWrapper<User>().in("user_id", userIds));
//
//        List<UserVo> userVos = users.stream().map(user -> {
//            UserVo userVo = new UserVo();
//            userVo.setUserId(user.getUserId());
//            userVo.setUsername(user.getUsername());
//            return userVo;
//        }).collect(Collectors.toList());
//        return userVos;
//    }
//
//
//    public Integer getGroupIdByInviteCode(String inviteCode) {
//        return groupMapper.findGroupIdByInviteCode(inviteCode);
//    }
//}
package com.u.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.u.constant.MessageConstant;
import com.u.context.BaseContext;
import com.u.domain.po.GroupMember;
import com.u.domain.po.GroupsInfo;
import com.u.domain.po.User;
import com.u.domain.vo.UserVo;
import com.u.mapper.GroupMemberMapper;
import com.u.mapper.GroupsInfoMapper;
import com.u.mapper.UserMapper;
import com.u.service.IGroupMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements IGroupMemberService {

    @Autowired
    private GroupsInfoMapper groupMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean joinGroup(String inviteCode, Integer userId) {
        log.info("用户[{}]尝试使用邀请码加入群组，邀请码：{}", userId, inviteCode);

        Integer groupId = getGroupIdByInviteCode(inviteCode);
        if (groupId == null) {
            log.warn("无效的邀请码：{}，用户ID：{}", inviteCode, userId);
            return false;
        }

        GroupMember groupMember = new GroupMember();
        groupMember.setUserId(userId);
        groupMember.setGroupId(groupId);
        groupMember.setEntergroupTime(LocalDateTime.now());

        int rowsAffected = groupMemberMapper.insert(groupMember);
        if (rowsAffected > 0) {
            log.info("用户[{}]成功加入群组[{}]", userId, groupId);
            return true;
        }
        log.error("群组成员记录插入失败，用户ID：{}，群组ID：{}", userId, groupId);
        return false;
    }

    @Override
    public List<GroupMember> getGroupsByUserId() {
        Integer userId = BaseContext.getCurrentId();
        log.debug("查询用户[{}]加入的群组列表", userId);

        return this.baseMapper.selectList(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getUserId, userId)
        );
    }

    @Override
    public List<UserVo> getUsersByGroupName(String groupName) {
        MDC.put("traceId", UUID.randomUUID().toString().substring(0,8));
        log.info("开始查询群组成员，群组名称：{} | 跟踪ID：{}",
                groupName, MDC.get("traceId"));
        log.info("开始查询群组成员，群组名称：{}", groupName);

        GroupsInfo group = groupMapper.selectOne(
                new LambdaQueryWrapper<GroupsInfo>()
                        .eq(GroupsInfo::getGroupName, groupName)
        );//MyBatis-Plus自动处理参数化查询，非Lambda表达式直接防注入
        if (group == null) {
            log.error("群组不存在：{}", groupName);
            throw new RuntimeException(MessageConstant.GROUP_NOT_EXIST);
        }

        List<GroupMember> groupMembers = groupMemberMapper.selectList(
                new LambdaQueryWrapper<GroupMember>()
                        .eq(GroupMember::getGroupId, group.getGroupId())
        );
        if (groupMembers.isEmpty()) {
            log.warn("群组[{}]暂无成员", groupName);
            throw new RuntimeException(MessageConstant.USER_NOT_EXIST);
        }

        List<Integer> userIds = groupMembers.stream()
                .map(GroupMember::getUserId)
                .collect(Collectors.toList());

        List<User> users = userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .in(User::getUserId, userIds)
        );

        List<UserVo> userVos = users.stream().map(user -> {
            UserVo userVo = new UserVo();
            userVo.setUserId(user.getUserId());
            userVo.setUsername(user.getUsername());
            return userVo;
        }).collect(Collectors.toList());

        log.info("成功获取群组[{}]成员，共{}人", groupName, userVos.size());
        MDC.remove("traceId");
        return userVos;
    }

    public Integer getGroupIdByInviteCode(String inviteCode) {
        log.debug("解析邀请码：{}", inviteCode);
        Integer groupId = groupMapper.findGroupIdByInviteCode(inviteCode);
        if (groupId == null) {
            log.warn("无效的邀请码：{}", inviteCode);
        }
        return groupId;
    }
}
