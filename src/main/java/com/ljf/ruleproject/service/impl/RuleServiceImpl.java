package com.ljf.ruleproject.service.impl;

import com.ljf.ruleproject.dao.RuleInfoDao;
import com.ljf.ruleproject.entity.DBInfo;
import com.ljf.ruleproject.entity.RuleInfo;
import com.ljf.ruleproject.service.RuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.lin on 2020/6/24
 */
@Service
public class RuleServiceImpl implements RuleService {

    @Resource
    private RuleInfoDao ruleInfoDao;

    private List<RuleInfo> ruleInfoList = new ArrayList<>();

    public RuleServiceImpl() {
        RuleInfo ruleInfo1 = new RuleInfo();
        ruleInfo1.setId(0);
        ruleInfo1.setDesc("积分兑换规则");
        ruleInfo1.setVersion("1.0");
        ruleInfo1.setRule("import com.ljf.ruleproject.poet.*;\n" +
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
        ruleInfo1.setInputDataDBInfo(inDBInfo);

        DBInfo outDBInfo = new DBInfo();
        outDBInfo.setDriverName("com.hxtt.sql.access.AccessDriver");
        outDBInfo.setDbURL("jdbc:Access:///D:/store.accdb");
        outDBInfo.setUserName("");
        outDBInfo.setUserPwd("");
        outDBInfo.setSql("update store");
        ruleInfo1.setOutputDataDBInfo(outDBInfo);

        ruleInfoList.add(ruleInfo1);
    }

    @Override
    public List<RuleInfo> findAll() {
        return ruleInfoList;
//        return ruleInfoDao.findAll();
    }

    @Override
    public RuleInfo getById(Integer id) {
        return ruleInfoList.get(id);
//        return ruleInfoDao.getById(id);
    }

    @Override
    public Integer add(RuleInfo ruleInfo) {
        ruleInfoList.add(ruleInfo);
        return 1;
    }
}
