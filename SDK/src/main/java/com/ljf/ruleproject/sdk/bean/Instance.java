package com.ljf.ruleproject.sdk.bean;

import lombok.Data;

/**
 * Created by mr.lin on 2020/7/3
 */
@Data
public class Instance {

    private String app;
    private String ipAddr;
    private String port;

    @Override
    public String toString() {
        return "Instance{" +
                "app='" + app + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
