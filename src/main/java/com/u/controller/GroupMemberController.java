package com.u.controller;


import com.u.constant.MessageConstant;
import com.u.context.BaseContext;
import com.u.domain.po.User;
import com.u.domain.vo.UserVo;
import com.u.result.Result;
import com.u.service.IGroupMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 群组成员表 前端控制器
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
@RestController
@RequestMapping("/group-member")
@Api(tags = "群组成员接口")
public class GroupMemberController {

    private static final Logger log = LoggerFactory.getLogger(GroupMemberController.class);
    @Autowired
    private IGroupMemberService groupService;

    /**
     * 加入群组
     *
     * @param inviteCode 群组邀请码
     * @return 返回加入群组的结果信息
     */
    @PostMapping("/join")
    @ApiOperation("加入群组")
    public Result<String> joinGroup(@RequestParam String inviteCode) {
        log.info("用户尝试通过邀请码加入群组，邀请码为：" + inviteCode);
        try {
            // 获取当前登录用户ID（通过BaseContext获取）
            Integer userId = BaseContext.getCurrentId();

            // 调用服务层逻辑处理加入群组
            boolean isJoined = groupService.joinGroup(inviteCode, userId);

            if (isJoined) {
                return Result.success(MessageConstant.JOIN_SUCCESS);
            } else {
                return Result.error(MessageConstant.INVALID_INVITE_CODE);  // 当邀请码无效时返回错误
            }
        } catch (Exception e) {
            // 返回失败信息
            return Result.error(MessageConstant.JOIN_FAILED + e.getMessage());
        }
    }

    /**
     * 根据群组名查询该群组所有成员的信息（用户ID和用户名）
     *
     * @param groupName 群组名
     * @return 返回群组成员信息
     */
    @GetMapping("/members")
    @ApiOperation("根据群组名查询成员")
    public Result<List<UserVo>> getUsersByGroupName(@RequestParam String groupName) {
        log.info("用户通过群组名查询群组成员信息，群组名为：" + groupName);
        try {
            List<UserVo> users = groupService.getUsersByGroupName(groupName);
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(MessageConstant.QUERY_FAILED + e.getMessage());
        }
    }
}
