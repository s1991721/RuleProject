package com.ljf.ruleproject.ruleEngine;

import lombok.Data;

/**
 * Created by mr.lin on 2020/6/24
 */
@Data
public class RuleInfo {

    private DBInfo ruleDBInfo;

    private DBInfo inputDataDBInfo;

    private DBInfo outputDataDBInfo;

}
