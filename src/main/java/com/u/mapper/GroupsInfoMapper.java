package com.u.mapper;

import com.u.domain.po.GroupsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 群组表 Mapper 接口
 * </p>
 *
 * @author u
 * @since 2024-12-24
 */
public interface GroupsInfoMapper extends BaseMapper<GroupsInfo> {

    /**
     * 通过邀请码查询群组ID
     * @param inviteCode 邀请码
     * @return 群组ID
     */
    @Select("SELECT group_id FROM groups_info WHERE invitation_code = #{inviteCode}")
    Integer findGroupIdByInviteCode(String inviteCode);
}
