package com.ljf.ruleproject.service;

import com.ljf.ruleproject.entity.RuleInfo;
import com.ljf.ruleproject.poet.Store;

import java.util.List;

/**
 * Created by mr.lin on 2020/7/1
 */
public interface DataService {

    void prepareData(RuleInfo ruleInfo);

    List<Store> getData();

    boolean saveData(List<Store> datas);

}
