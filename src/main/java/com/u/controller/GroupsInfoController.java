package com.u.controller;


import com.u.constant.MessageConstant;
import com.u.domain.po.GroupMember;
import com.u.domain.po.GroupsInfo;
import com.u.domain.vo.GroupInfoVO;
import com.u.result.Result;
import com.u.service.IGroupMemberService;
import com.u.service.IGroupsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.u.context.BaseContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 群组表 前端控制器
 * </p>
 *
 * @author u
 * @since 2024-12-24
 */
@Slf4j
@RestController
@RequestMapping("/groups")
@Api(tags = "群组接口")
public class GroupsInfoController {

    @Autowired
    private IGroupsInfoService groupService;

    @Autowired
    private IGroupMemberService groupMemberService;  // 群组成员服务
    @ApiOperation(value = "查询群组")
    @GetMapping("/userGroups")
    public Result<List<String>> getUserGroups() {


        // 1. 查询该用户加入的所有群组的group_id
        List<GroupMember> groupMembers = groupMemberService.getGroupsByUserId();

        if (groupMembers == null || groupMembers.isEmpty()) {
            return Result.error("用户没有加入任何群组");
        }

        // 2. 获取所有的group_id
        List<Integer> groupIds = groupMembers.stream()
                .map(GroupMember::getGroupId)  // 提取所有group_id
                .collect(Collectors.toList());

        // 3. 根据group_id查询群组名称
        List<GroupsInfo> groupsInfoList = groupService.getGroupsByGroupIds(groupIds);

        if (groupsInfoList != null && !groupsInfoList.isEmpty()) {
            // 返回群组名称列表
            List<String> groupNames = groupsInfoList.stream()
                    .map(GroupsInfo::getGroupName)  // 提取群组名称
                    .collect(Collectors.toList());

            return Result.success(groupNames);  // 返回成功结果，包含群组名称列表
        } else {
            // 如果没有找到群组信息，返回失败
            return Result.error( "未找到对应的群组信息");
        }
    }


    /**
     * 创建群组
     *
     * @param groupName 群组名称
     * @return 返回创建是否成功的消息
     */
    @PostMapping("/create")
    @ApiOperation("创建群组")
    public Result<String> createGroup(@RequestParam String groupName) {
        log.info("创建群组：" + groupName);
        try {
            // 获取当前登录用户ID（通过BaseContext获取）
            Integer userId = BaseContext.getCurrentId();

            // 调用服务层逻辑创建群组
            groupService.createGroup(groupName, userId);

            // 返回成功信息
            return Result.success(MessageConstant.CREATE_SUCCESS);
        } catch (Exception e) {
            // 返回失败信息
            return Result.error(MessageConstant.CREATE_FAILED + e.getMessage());
        }
    }

    @ApiOperation(value = "查询用户管理员的群组")
    @GetMapping("/adminGroups")
    public Result<List<GroupInfoVO>> getAdministratorGroups() {
        // 调用 Service 层获取当前用户作为管理员的所有群组信息
        List<GroupInfoVO> groupInfoVOList = groupService.getAdministratorGroups();

        // 如果没有查询到群组信息，返回错误信息
        if (groupInfoVOList == null || groupInfoVOList.isEmpty()) {
            return Result.error("用户没有管理员权限的群组");
        }

        // 返回成功结果
        return Result.success(groupInfoVOList);
    }
}


