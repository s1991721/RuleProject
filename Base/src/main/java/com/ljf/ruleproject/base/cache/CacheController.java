package com.ljf.ruleproject.base.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mr.lin on 2020/7/15
 */
@RestController
public class CacheController {

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/putCache")
//    @Cacheable(value = "userCache",key = "cache")
    public String put(){
        return userService.testEhcache("").toString();
    }

    @GetMapping("/getCache")
    public String get(){
        return userService.getUser("zhagnsan").toString();
    }

}
