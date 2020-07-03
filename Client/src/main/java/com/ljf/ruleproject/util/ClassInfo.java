package com.ljf.ruleproject.util;

import lombok.Data;

import java.util.List;

/**
 * Created by mr.lin on 2020/7/2
 * 类信息
 */
@Data
public class ClassInfo {

    private String name;

    private String packageName;

    private List<Attr> attrList;

}
