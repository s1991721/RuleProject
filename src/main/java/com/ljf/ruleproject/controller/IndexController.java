package com.ljf.ruleproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by mr.lin on 2020/6/24
 */
@Controller
public class IndexController {

    @GetMapping("home")
    public String index() {
        return "index";
    }

}
