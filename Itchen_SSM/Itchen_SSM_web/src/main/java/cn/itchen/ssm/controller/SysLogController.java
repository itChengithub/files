package cn.itchen.ssm.controller;

import cn.itchen.ssm.domain.SysLog;
import cn.itchen.ssm.service.SysLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/sysLog")
public class SysLogController {
    @Autowired
    private SysLogService service;
    @RequestMapping("/findAll")
    public ModelAndView findAll(@RequestParam(value = "page",required = true,defaultValue = "1")Integer page,@RequestParam(value = "size",required = true,defaultValue = "5") Integer size){
        List<SysLog> sysLogList=service.findAll(page,size);
        PageInfo<SysLog> pageInfo = new PageInfo<>(sysLogList);
        pageInfo.setSize(size);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.setViewName("log-list");
        return modelAndView;
    }
}
