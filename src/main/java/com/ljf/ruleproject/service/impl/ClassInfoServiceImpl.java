package com.ljf.ruleproject.service.impl;

import com.ljf.ruleproject.dao.ClassInfoDao;
import com.ljf.ruleproject.service.ClassInfoService;
import com.ljf.ruleproject.util.ClassCreator;
import com.ljf.ruleproject.util.ClassInfo;
import com.ljf.ruleproject.util.JavaPoet;
import com.squareup.javapoet.JavaFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mr.lin on 2020/7/2
 */
@Service
@Slf4j
public class ClassInfoServiceImpl implements ClassInfoService {

    @Resource
    private ClassInfoDao classInfoDao;

    private Map<String, Class<?>> classMap = new HashMap<>();

    @Override
    public void prepareClass() {
        // TODO: 2020/7/2 根据typeId查找
        ClassInfo classInfo = classInfoDao.getClassInfo(0);
        JavaFile javaFile = null;
        try {
            javaFile = JavaPoet.createJavaFile(classInfo);
        } catch (ClassNotFoundException e) {
            log.error("生成JavaFile错误，请检查字段信息");
            e.printStackTrace();
            return;
        }

        Class<?> clazz = null;
        try {
            clazz = ClassCreator.createClass(javaFile);
        } catch (URISyntaxException e) {
            log.error("生成类失败");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.error("加载生成类失败");
            e.printStackTrace();
        }
        classMap.put(classInfo.getName(), clazz);
    }

    @Override
    public Class<?> getClassByName(String name) {
        return classMap.get(name);
    }

}
