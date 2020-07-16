package com.ljf.ruleproject.base.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mr.lin on 2020/7/15
 */
@Service
public class UserServiceImpl {

    public static final String CACHE_NAME = "userCache";

    @Autowired
    private UserDao userDao;

    @Autowired
    private EhcacheServiceComponent ehcacheServiceComponent;

    @Autowired
    private RedisServiceComponent redisServiceComponent;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    public User getUser(String name) {
        //首先从本地ehcach中取数据
        User user = (User) ehcacheServiceComponent.get(CACHE_NAME, name);
        if (!StringUtils.isEmpty(user)) {
            //如果存在，则直接返回，如果不存在则从redis中取数据
            System.out.println("我是从ehcache中查出来的: ======= >>>" + name);
            return user;
        }
        //如果redis有则直接返回，如果redis 没有则查询数据
        user = (User) redisServiceComponent.get(name);
        //如果数据库查询不为空则，则讲返回结果缓存到本地和redis缓存中
        if (user != null) {
            System.out.println("我是从redis中查出来的: ======= >>>" + name);
            return user;
        }
        //redis也为空则从数据库中获取
        User dbInfo = userDao.getUser(name);
        if (dbInfo != null) {
            //缓存到redis中
            redisServiceComponent.set(name, dbInfo, 20);
//            redisTemplate.opsForValue().set(name, JSONObject.toJSONString(dbInfo),10);
            System.out.println("我是从mysql中查出来的: ======= >>>" + name);
            //缓存到本地ehcache中
//                    productEcacheService.saveLocalCache(dbInfo);
            ehcacheServiceComponent.put(CACHE_NAME, name, dbInfo);
            return dbInfo;
        } else {
            return null;
        }
    }

    @Cacheable(value = CACHE_NAME, key = "#p0")
    public User testEhcache(String name) {
        System.out.println("没有缓存则执行如下代码");
        return new User(1, "zhagnsan", 10);
    }
}
