package com.ljf.ruleproject.service;

import com.ljf.ruleproject.entity.RuleInfo;

import java.util.List;

/**
 * Created by mr.lin on 2020/6/24
 */
public interface RuleService {

    List<RuleInfo> findAll();

    RuleInfo getById(Integer id);

    Integer add(RuleInfo ruleInfo);

}
