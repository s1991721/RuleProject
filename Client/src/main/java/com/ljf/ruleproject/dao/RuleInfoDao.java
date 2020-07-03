package com.ljf.ruleproject.dao;

import com.ljf.ruleproject.entity.DBInfo;
import com.ljf.ruleproject.entity.RuleInfo;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.lin on 2020/6/26
 */
@Component
public class RuleInfoDao {

    private Connection con;

    public RuleInfoDao() {
        con = null;
        try {
            Class.forName("com.hxtt.sql.access.AccessDriver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            con = (Connection) DriverManager.getConnection("jdbc:Access:///D:/rule.accdb", "", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } // 连接数据库

    }

    public List<RuleInfo> findAll() {

        List<RuleInfo> ruleInfoList = new ArrayList<>();
        try {
            Statement sta = null;
            sta = con.createStatement();

            ResultSet rst = null;//demoTable为access数据库中的一个表名

            rst = sta.executeQuery("select * from rule_info");


            while (rst.next()) {
                RuleInfo ruleInfo = new RuleInfo();
                ruleInfo.setId(rst.getInt("id"));
                ruleInfo.setRule(rst.getString("rule"));
                ruleInfo.setVersion(rst.getString("version"));
                ruleInfo.setDesc(rst.getString("desc"));

                DBInfo inputDBInfo = new DBInfo();
                inputDBInfo.setDriverName(rst.getString("in_driver_name"));
                inputDBInfo.setDbURL(rst.getString("in_dburl"));
                inputDBInfo.setUserName(rst.getString("in_user_name"));
                inputDBInfo.setUserPwd(rst.getString("in_user_password"));
                inputDBInfo.setSql(rst.getString("in_sql"));
                ruleInfo.setInputDataDBInfo(inputDBInfo);

                DBInfo outputDBInfo = new DBInfo();
                outputDBInfo.setDriverName(rst.getString("out_driver_name"));
                outputDBInfo.setDbURL(rst.getString("out_dburl"));
                outputDBInfo.setUserName(rst.getString("out_user_name"));
                outputDBInfo.setUserPwd(rst.getString("out_user_password"));
                outputDBInfo.setSql(rst.getString("out_sql"));
                ruleInfo.setOutputDataDBInfo(outputDBInfo);

                ruleInfoList.add(ruleInfo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ruleInfoList;
    }

    public RuleInfo getById(Integer id) {
        RuleInfo ruleInfo = null;
        try {
            Statement sta = null;
            sta = con.createStatement();

            ResultSet rst = null;//demoTable为access数据库中的一个表名

            rst = sta.executeQuery("select * from rule_info where id =" + id);

            if (rst.next()) {
                ruleInfo = new RuleInfo();
                ruleInfo.setId(rst.getInt("id"));
                ruleInfo.setRule(rst.getString("rule"));
                ruleInfo.setVersion(rst.getString("version"));
                ruleInfo.setDesc(rst.getString("desc"));

                DBInfo inputDBInfo = new DBInfo();
                inputDBInfo.setDriverName(rst.getString("in_driver_name"));
                inputDBInfo.setDbURL(rst.getString("in_dburl"));
                inputDBInfo.setUserName(rst.getString("in_user_name"));
                inputDBInfo.setUserPwd(rst.getString("in_user_password"));
                inputDBInfo.setSql(rst.getString("in_sql"));
                ruleInfo.setInputDataDBInfo(inputDBInfo);

                DBInfo outputDBInfo = new DBInfo();
                outputDBInfo.setDriverName(rst.getString("out_driver_name"));
                outputDBInfo.setDbURL(rst.getString("out_dburl"));
                outputDBInfo.setUserName(rst.getString("out_user_name"));
                outputDBInfo.setUserPwd(rst.getString("out_user_password"));
                outputDBInfo.setSql(rst.getString("out_sql"));
                ruleInfo.setOutputDataDBInfo(outputDBInfo);

            }
        } catch (SQLException throwables) {

            throwables.printStackTrace();
        }
        return ruleInfo;
    }

    public static void main(String[] args) {
        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setId(8);
        ruleInfo.setRule("aaa");
        ruleInfo.setVersion("bbb");
        ruleInfo.setDesc("ccc");

        DBInfo inDBInfo = new DBInfo();
        inDBInfo.setDriverName("ddd");
        inDBInfo.setDbURL("eee");
        inDBInfo.setUserName("fff");
        inDBInfo.setUserPwd("ggg");
        inDBInfo.setSql("hhh");
        ruleInfo.setInputDataDBInfo(inDBInfo);

        DBInfo outDBInfo = new DBInfo();
        outDBInfo.setDriverName("iii");
        outDBInfo.setDbURL("jjj");
        outDBInfo.setUserName("kkk");
        outDBInfo.setUserPwd("lll");
        outDBInfo.setSql("mmm");
        ruleInfo.setOutputDataDBInfo(outDBInfo);

        RuleInfoDao ruleInfoDao = new RuleInfoDao();
        ruleInfoDao.insert(ruleInfo);
    }

    public int insert(RuleInfo ruleInfo) {
        return 1;
    }
}
