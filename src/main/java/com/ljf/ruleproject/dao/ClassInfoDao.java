package com.ljf.ruleproject.dao;

import com.ljf.ruleproject.util.ClassInfo;
import org.springframework.stereotype.Component;

/**
 * Created by mr.lin on 2020/7/2
 */
@Component
public class ClassInfoDao {

    public ClassInfo getClassInfo(Integer id){
        return new ClassInfo();
    }

}
