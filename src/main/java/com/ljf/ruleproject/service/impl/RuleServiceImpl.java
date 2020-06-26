package com.ljf.ruleproject.service.impl;

import com.ljf.ruleproject.entity.RuleInfo;
import com.ljf.ruleproject.dao.RuleInfoDao;
import com.ljf.ruleproject.service.RuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by mr.lin on 2020/6/24
 */
@Service
public class RuleServiceImpl implements RuleService {

    @Resource
    private RuleInfoDao ruleInfoDao;

    @Override
    public List<RuleInfo> findAll() {
        return ruleInfoDao.findAll();
    }
}
