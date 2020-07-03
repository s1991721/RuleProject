package com.ljf.ruleproject.config;

import com.ljf.ruleproject.quartz.UpdateCacheJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * Created by mr.lin on 2020/7/2
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(UpdateCacheJob.class).storeDurably().build();
    }

    @Bean
    public Trigger trigger() {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(10)
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withSchedule(simpleScheduleBuilder)
                .startAt(new Date(System.currentTimeMillis() + 60 * 1000))
                .build();
    }

}
