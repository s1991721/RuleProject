package com.ljf.ruleproject.base.cache;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by mr.lin on 2020/7/15
 */
@Data
public class User implements Serializable {

    private int id;
    private String name;
    private int age;

    public User() {
    }

    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
