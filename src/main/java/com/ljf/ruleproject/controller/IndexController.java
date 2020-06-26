package com.ljf.ruleproject.controller;

import com.ljf.ruleproject.service.RuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * Created by mr.lin on 2020/6/24
 */
@Controller
public class IndexController {

    @Resource
    private RuleService ruleService;



    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("ruleList",ruleService.findAll());
        return "edit";
    }

}
