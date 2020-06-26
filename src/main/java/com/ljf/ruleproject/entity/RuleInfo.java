package com.ljf.ruleproject.entity;

import lombok.Data;

/**
 * Created by mr.lin on 2020/6/24
 */
@Data
public class RuleInfo {

    private Integer id;

    private String rule;

    private String version;

    private String desc;

    private DBInfo inputDataDBInfo;

    private DBInfo outputDataDBInfo;

}
