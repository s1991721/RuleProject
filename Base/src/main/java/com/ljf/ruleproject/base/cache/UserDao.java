package com.ljf.ruleproject.base.cache;

import org.springframework.stereotype.Component;

/**
 * Created by mr.lin on 2020/7/15
 */
@Component
public class UserDao {

    public User getUser(String name){
        return new User(2, "lisi", 20);
    }

}
