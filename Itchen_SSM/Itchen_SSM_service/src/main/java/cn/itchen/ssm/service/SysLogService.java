package cn.itchen.ssm.service;

import cn.itchen.ssm.domain.SysLog;

import java.util.List;

public interface SysLogService {
    void saveLog(SysLog sysLog);

    List<SysLog> findAll(Integer page, Integer size);

}
