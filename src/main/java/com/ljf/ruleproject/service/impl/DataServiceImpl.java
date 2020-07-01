package com.ljf.ruleproject.service.impl;

import com.ljf.ruleproject.entity.DBInfo;
import com.ljf.ruleproject.entity.RuleInfo;
import com.ljf.ruleproject.poet.Store;
import com.ljf.ruleproject.service.DataService;
import com.ljf.ruleproject.util.ResultSetToBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

/**
 * Created by mr.lin on 2020/7/1
 */
@Service
@Slf4j
public class DataServiceImpl implements DataService {

    private RuleInfo ruleInfo;
    private DBInfo inDBInfo;
    private DBInfo outDBInfo;
    private Connection inConnection;
    private Connection outConnection;

    @CachePut(value = "cache", key = "'input_datas'")
    @Override
    public List<Store> getData() {
        String sql = inDBInfo.getSql();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = inConnection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            log.error("获取业务数据异常{}", throwables.toString());
            throwables.printStackTrace();
        }
        if (resultSet == null) {
            log.info("未获取到业务数据");
            return null;
        }

        List<Store> storeList = null;
        try {
            storeList = ResultSetToBean.coverList(resultSet);
        } catch (SQLException throwables) {
            log.info("业务数据转换失败");
            throwables.printStackTrace();
        }

        try {
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            log.info("获取数据：关闭流失败{}", throwables.toString());
            throwables.printStackTrace();
        }
        return storeList;
    }

    @Override
    public boolean saveData(List<Store> datas) {
        String sql = outDBInfo.getSql();
        // TODO: 2020/7/1 自动生成SQL

        int affect = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = outConnection.prepareStatement(sql);
            affect = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException throwables) {
            log.info("保存数据：关闭流失败{}", throwables.toString());
            throwables.printStackTrace();
        }
        return affect != 0;
    }

    @Override
    public void prepareData(RuleInfo ruleInfo) {
        this.ruleInfo = ruleInfo;
        inDBInfo = ruleInfo.getInputDataDBInfo();
        outDBInfo = ruleInfo.getOutputDataDBInfo();
        inConnection = getConnection(inDBInfo);
        outConnection = getConnection(outDBInfo);
    }

    private Connection getConnection(DBInfo dbInfo) {
        log.info(dbInfo.toString());
        Connection dbConn = null;
        String driverName = dbInfo.getDriverName();
        String dbURL = dbInfo.getDbURL();
        String userName = dbInfo.getUserName();
        String userPwd = dbInfo.getUserPwd();

        try {
            Class.forName(driverName);
            dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
        } catch (ClassNotFoundException e) {
            log.info("驱动加载失败");
            e.printStackTrace();
        } catch (SQLException e) {
            log.info("连接失败");
            e.printStackTrace();
        }
        return dbConn;
    }

}
