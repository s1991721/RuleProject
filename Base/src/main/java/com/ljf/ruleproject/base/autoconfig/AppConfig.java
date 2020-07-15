package com.ljf.ruleproject.base.autoconfig;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mr.lin on 2020/7/14
 * Apollo自动配置
 */
@Configuration
@EnableApolloConfig
public class AppConfig {

    @Bean
    public TestJavaConfigBean javaConfigBean(){
        return new TestJavaConfigBean();
    }

}
