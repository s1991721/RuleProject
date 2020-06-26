package com.ljf.ruleproject.entity;

import lombok.Data;

import java.lang.reflect.Type;

/**
 * Created by mr.lin on 2020/6/24
 */
@Data
public class DBInfo {

    private String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String dbURL="jdbc:sqlserver://192.168.64.14";
    private String userName= "sa";
    private String userPwd= "sa";
    private String sql;
    private Type classType;

}
