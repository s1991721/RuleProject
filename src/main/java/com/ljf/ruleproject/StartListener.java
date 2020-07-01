package com.ljf.ruleproject;

import com.ljf.ruleproject.entity.RuleInfo;
import com.ljf.ruleproject.service.DataService;
import com.ljf.ruleproject.service.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by mr.lin on 2020/7/1
 * 启动监听
 */
@Component
@Slf4j
public class StartListener implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    RuleService ruleService;
    @Resource
    DataService dataService;

    @Value(value = "${rule.id}")
    private Integer ruleId;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("项目启动成功{}", ruleId);

        // 加载规则
        RuleInfo ruleInfo = ruleService.getById(ruleId);
        log.info(ruleInfo.toString());
        // 缓存数据
        dataService.prepareData(ruleInfo);

        // TODO: 2020/7/1 开启定时任务缓存数据


    }


}
