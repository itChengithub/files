package cn.itchen.ssm.dao;

import cn.itchen.ssm.domain.SysLog;

import java.util.List;

/**
 * Log操作的持久层接口
 */
public interface SysLogDao {
    void saveLog(SysLog sysLog);

    List<SysLog> findAll();

}
