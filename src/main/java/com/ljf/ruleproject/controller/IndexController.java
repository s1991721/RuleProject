package com.ljf.ruleproject.controller;

import com.ljf.ruleproject.ruleEngine.RuleExecutor;
import com.ljf.ruleproject.ruleEngine.RuleInfo;
import com.ljf.ruleproject.ruleEngine.RuleThreadPool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by mr.lin on 2020/6/24
 */
@Controller
public class IndexController {

    @GetMapping("home")
    public String index() {


        RuleThreadPool.submit(new RuleExecutor(new RuleInfo()));


        return "index";
    }

    

}
