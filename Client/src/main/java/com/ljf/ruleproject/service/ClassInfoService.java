package com.ljf.ruleproject.service;

/**
 * Created by mr.lin on 2020/7/2
 */
public interface ClassInfoService {

    void prepareClass();

    Class<?> getClassByName(String name);

}
