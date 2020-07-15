package com.ljf.ruleproject.base.autoconfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by mr.lin on 2020/7/14
 */
@Data
public class TestJavaConfigBean {
    @Value("${timeout:100}")
    private String timeout;

    @Value("${batch:200}")
    private String batch;


}
