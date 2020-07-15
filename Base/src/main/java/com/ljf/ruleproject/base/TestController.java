package com.ljf.ruleproject.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mr.lin on 2020/7/14
 */
@RestController
public class TestController {

    @Autowired
    private AppConfig appConfig;

    @RequestMapping("get")
    public String get() {
        TestJavaConfigBean bean = appConfig.javaConfigBean();
        return bean.toString();
    }

}
