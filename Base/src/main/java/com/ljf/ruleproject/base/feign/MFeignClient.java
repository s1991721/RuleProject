package com.ljf.ruleproject.base.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by mr.lin on 2020/7/16
 */
@FeignClient("test")
public interface MFeignClient {

    @GetMapping("/getCache")
    String getCache();

}
