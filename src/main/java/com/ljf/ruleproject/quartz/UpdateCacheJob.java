package com.ljf.ruleproject.quartz;

import com.ljf.ruleproject.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 * Created by mr.lin on 2020/7/2
 * 更新业务缓存数据
 */
@Slf4j
public class UpdateCacheJob extends QuartzJobBean {

    @Resource
    private DataService dataService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("定时任务：刷新业务数据");
        dataService.getData();
    }
}
