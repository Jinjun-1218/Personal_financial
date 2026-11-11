package com.u.service;

import com.u.domain.po.GroupMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.u.domain.vo.UserVo;

import java.util.List;

/**
 * <p>
 * 群组成员表 服务类
 * </p>
 *
 * @author u
 * @since 2024-12-05
 */
public interface IGroupMemberService extends IService<GroupMember> {

    /**
     * 通过邀请码查找群组ID
     * @param inviteCode 邀请码
     * @return 群组ID
     */
    Integer getGroupIdByInviteCode(String inviteCode);
    /**
     * 加入群组
     * @param inviteCode
     * @param userId
     * @return
     */
    boolean joinGroup(String inviteCode, Integer userId);

    /**
     * 根据用户ID获取该用户加入的群组ID列表
     * @return
     */
    List<GroupMember> getGroupsByUserId();

    /**
     * 根据群组名获取群组成员
     * @param groupName
     * @return
     */
    List<UserVo> getUsersByGroupName(String groupName);
}
