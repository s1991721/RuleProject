package com.ljf.ruleproject.sdk.bean;

import lombok.Data;

import java.util.List;

/**
 * Created by mr.lin on 2020/7/3
 */
@Data
public class Application {

    private String name;
    private List<Instance> instance;

    @Override
    public String toString() {
        return "Application{" +
                "name='" + name + '\'' +
                ", instance=" + instance +
                '}';
    }
}
