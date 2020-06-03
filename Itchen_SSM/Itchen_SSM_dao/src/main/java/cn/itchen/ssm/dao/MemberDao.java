package cn.itchen.ssm.dao;

import cn.itchen.ssm.domain.Member;

/**
 * 会员的dao持久层
 */
public interface MemberDao {
    /**
     * 根据会员id查询会员信息
     * @param id
     * @return
     */
    Member findbyId(String id);
}
