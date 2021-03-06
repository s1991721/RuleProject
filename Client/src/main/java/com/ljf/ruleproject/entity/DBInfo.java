package com.ljf.ruleproject.entity;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by mr.lin on 2020/6/24
 */
@Data
public class DBInfo implements Serializable {

    private String driverName;
    private String dbURL;
    private String userName;
    private String userPwd;
    private String sql;
    private Type classType;

}
