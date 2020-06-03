package cn.itchen.ssm.service.impl;

import cn.itchen.ssm.dao.SysLogDao;
import cn.itchen.ssm.domain.SysLog;
import cn.itchen.ssm.service.SysLogService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogDao logDao;
    @Override
    public void saveLog(SysLog sysLog) {
        logDao.saveLog(sysLog);
        System.out.println("Service执行了保存Log");
    }

    @Override
    public List<SysLog> findAll(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        return logDao.findAll();
    }
}
