package com.ljf.ruleproject.controller;

import com.ljf.ruleproject.entity.DBInfo;
import com.ljf.ruleproject.entity.RuleInfo;
import com.ljf.ruleproject.ruleEngine.RuleExecutor;
import com.ljf.ruleproject.ruleEngine.RuleThreadPool;
import com.ljf.ruleproject.service.RuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by mr.lin on 2020/6/24
 */
@Controller
public class IndexController {

    @Resource
    private RuleService ruleService;

    @GetMapping("home")
    public String index() {
        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setRule("import com.ljf.ruleproject.poet.*;\n" +
                "\n" +
                "dialect  \"mvel\"\n" +
                "\n" +
                "rule \"reward\"\n" +
                "    when\n" +
                "        $store: Store(\n" +
                "        attribute==\"CAC\", //店铺属性\n" +
                "        signValue>300,// 签约量大于签约级别值\n" +
                "        figure==\"CS900\",// 花纹匹配\n" +
                "        size==\"R20\", //寸别匹配\n" +
                "        sales>50 //销量>签约标准\n" +
                "        )\n" +
                "    then\n" +
                "        Integer sum =$store.getSales()*5;\n" +
                "        Integer integral=sum*50/$store.getSales();\n" +
                "        integral=integral-$store.getReturns()*6;\n" +
                "        $store.setIntegral(integral);\n" +
                "        update($store)\n" +
                "end\n");

        DBInfo inDBInfo = new DBInfo();
        inDBInfo.setDriverName("com.hxtt.sql.access.AccessDriver");
        inDBInfo.setDbURL("jdbc:Access:///D:/store.accdb");
        inDBInfo.setUserName("");
        inDBInfo.setUserPwd("");
        inDBInfo.setSql("select * from store;");
        ruleInfo.setInputDataDBInfo(inDBInfo);

        DBInfo outDBInfo = new DBInfo();
        outDBInfo.setDriverName("com.hxtt.sql.access.AccessDriver");
        outDBInfo.setDbURL("jdbc:Access:///D:/store.accdb");
        outDBInfo.setUserName("");
        outDBInfo.setUserPwd("");
        outDBInfo.setSql("update store set integral = $1 where id= $2 ;");
        ruleInfo.setOutputDataDBInfo(outDBInfo);

        RuleThreadPool.submit(new RuleExecutor(ruleInfo));
        return "index";
    }

    public static void main(String[] args) {

        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setRule("import com.ljf.ruleproject.poet.*;\n" +
                "\n" +
                "dialect  \"mvel\"\n" +
                "\n" +
                "rule \"reward\"\n" +
                "    when\n" +
                "        $store: Store(\n" +
                "        attribute==\"CAC\", //店铺属性\n" +
                "        signValue>300,// 签约量大于签约级别值\n" +
                "        figure==\"CS900\",// 花纹匹配\n" +
                "        size==\"R20\", //寸别匹配\n" +
                "        sales>50 //销量>签约标准\n" +
                "        )\n" +
                "    then\n" +
                "        Integer sum =$store.getSales()*5;\n" +
                "        Integer integral=sum*50/$store.getSales();\n" +
                "        integral=integral-$store.getReturns()*6;\n" +
                "        $store.setIntegral(integral);\n" +
                "        update($store)\n" +
                "end\n");

        DBInfo inDBInfo = new DBInfo();
        inDBInfo.setDriverName("com.hxtt.sql.access.AccessDriver");
        inDBInfo.setDbURL("jdbc:Access:///D:/store.accdb");
        inDBInfo.setUserName("");
        inDBInfo.setUserPwd("");
        inDBInfo.setSql("select * from store;");
        ruleInfo.setInputDataDBInfo(inDBInfo);

        DBInfo outDBInfo = new DBInfo();
        outDBInfo.setDriverName("com.hxtt.sql.access.AccessDriver");
        outDBInfo.setDbURL("jdbc:Access:///D:/store.accdb");
        outDBInfo.setUserName("");
        outDBInfo.setUserPwd("");
        outDBInfo.setSql("update store");
        ruleInfo.setOutputDataDBInfo(outDBInfo);

        RuleThreadPool.submit(new RuleExecutor(ruleInfo));
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<RuleInfo> ruleInfoList = ruleService.findAll();
        model.addAttribute("ruleList", ruleInfoList);
        return "edit";
    }

    @GetMapping("/index/{id}")
    public String index(Model model, @PathVariable(value = "id") int id) {
        RuleInfo ruleInfo = ruleService.getById(id);
        model.addAttribute("rule", ruleInfo);
        return "detail";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("ruleInfo", new RuleInfo());
        return "add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute RuleInfo ruleInfo, Model model) {
        if (ruleService.add(ruleInfo) != 0) {
            model.addAttribute("status", "成功");
        } else {
            model.addAttribute("status", "失败");
        }
        return "status";
    }

    @GetMapping("/execute/{id}")
    public String execute(Model model, @PathVariable(value = "id") int id){
        RuleInfo ruleInfo = ruleService.getById(id);
        model.addAttribute("ruleInfo",ruleInfo);
        return "execute";
    }


}
