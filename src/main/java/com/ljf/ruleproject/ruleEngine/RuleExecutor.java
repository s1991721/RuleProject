package com.ljf.ruleproject.ruleEngine;

import com.ljf.ruleproject.entity.DBInfo;
import com.ljf.ruleproject.entity.RuleInfo;
import com.ljf.ruleproject.poet.Store;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.event.process.*;
import org.kie.api.event.rule.*;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.lin on 2020/6/24
 */
@Slf4j
public class RuleExecutor implements Runnable {

    private RuleInfo ruleInfo;

    public RuleExecutor(RuleInfo ruleInfo) {
        this.ruleInfo = ruleInfo;
    }

    @Override
    public void run() {
        log.info("规则开始执行");
        String rule = getRule();

        log.info("获取到规则：{}", rule);
        KieHelper helper = new KieHelper();

        helper.addContent(rule, ResourceType.DRL);

        KieSession ksession = helper.build().newKieSession();

        //规则执行，关键过程回调
        ksession.addEventListener(new RuleRuntimeEventListener() {
            @Override
            public void objectInserted(ObjectInsertedEvent event) {

            }

            @Override
            public void objectUpdated(ObjectUpdatedEvent event) {

            }

            @Override
            public void objectDeleted(ObjectDeletedEvent event) {

            }
        });
        ksession.addEventListener(new AgendaEventListener() {
            @Override
            public void matchCreated(MatchCreatedEvent event) {

            }

            @Override
            public void matchCancelled(MatchCancelledEvent event) {

            }

            @Override
            public void beforeMatchFired(BeforeMatchFiredEvent event) {

            }

            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {

            }

            @Override
            public void agendaGroupPopped(AgendaGroupPoppedEvent event) {

            }

            @Override
            public void agendaGroupPushed(AgendaGroupPushedEvent event) {

            }

            @Override
            public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {

            }

            @Override
            public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {

            }

            @Override
            public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {

            }

            @Override
            public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {

            }
        });
        ksession.addEventListener(new ProcessEventListener() {
            @Override
            public void beforeProcessStarted(ProcessStartedEvent event) {

            }

            @Override
            public void afterProcessStarted(ProcessStartedEvent event) {

            }

            @Override
            public void beforeProcessCompleted(ProcessCompletedEvent event) {

            }

            @Override
            public void afterProcessCompleted(ProcessCompletedEvent event) {

            }

            @Override
            public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {

            }

            @Override
            public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {

            }

            @Override
            public void beforeNodeLeft(ProcessNodeLeftEvent event) {

            }

            @Override
            public void afterNodeLeft(ProcessNodeLeftEvent event) {

            }

            @Override
            public void beforeVariableChanged(ProcessVariableChangedEvent event) {

            }

            @Override
            public void afterVariableChanged(ProcessVariableChangedEvent event) {

            }
        });

        List<Store> datas = getData();

        log.info("获取到数据：{}，开始插入数据", datas.toString());
        for (Object data : datas) {
            ksession.insert(data);
        }

        log.info("插入数据完成");
        log.info("执行规则开始");
        ksession.fireAllRules();

        log.info("执行规则结束");
        ksession.dispose();

        log.info("结果存库开始");
        saveData(datas);

        log.info("结果存库结束");
    }

    /**
     * 获取规则
     *
     * @return
     */
    private String getRule() {

        return ruleInfo.getRule();
    }

    /**
     * 获取数据
     * 通过JavaPoet生成类文件
     * 后期本地缓存数据
     *
     * @return
     */
    private List getData() {
        return getDataFromDB(ruleInfo.getInputDataDBInfo());
    }

    /**
     * 从数据库获取数据
     *
     * @param dbInfo 数据库信息
     * @return
     */
    private List getDataFromDB(DBInfo dbInfo) {

        Connection dbConn = getDBConnection(dbInfo);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dbConn.prepareStatement(dbInfo.getSql());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (resultSet == null) {
            return null;
        }
        //ResultSet 转实体类
        List dataList = new ArrayList();
        try {
            while (resultSet.next()) {
                Store data = new Store();
                data.setId(resultSet.getString("id"));
                data.setSignValue(resultSet.getInt("sign_value"));
                data.setAttribute(resultSet.getString("attribute"));
                data.setFigure(resultSet.getString("figure"));
                data.setSize(resultSet.getString("size"));
                data.setSales(resultSet.getInt("sales"));
                data.setReturns(resultSet.getInt("returns"));
                dataList.add(data);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dataList;
    }

    /**
     * //todo 后期缓存使用
     *
     * @return
     */
    private List getDataFromCache() {

        return null;
    }

    /**
     * DB连接
     *
     * @param dbInfo 连接信息
     * @return
     */
    private Connection getDBConnection(DBInfo dbInfo) {

        String driverName = dbInfo.getDriverName();

        String dbURL = dbInfo.getDbURL();

        String userName = dbInfo.getUserName();

        String userPwd = dbInfo.getUserPwd();

        Connection dbConn = null;

        try {
            Class.forName(driverName);
            dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
        } catch (ClassNotFoundException e) {
            //驱动加载失败
            e.printStackTrace();
        } catch (SQLException e) {
            //连接失败
            e.printStackTrace();
        }
        return dbConn;
    }

    /**
     * 执行结果存库
     *
     * @param datas 结果数据
     */
    private void saveData(List<Store> datas) {

        Connection dbConn = getDBConnection(ruleInfo.getOutputDataDBInfo());
        PreparedStatement preparedStatement = null;


        try {
            StringBuilder sql = new StringBuilder();

            for (Store store : datas) {
                StringBuilder sqlItem = new StringBuilder(ruleInfo.getOutputDataDBInfo().getSql());
                sqlItem.append(" set integral = ")
                        .append(store.getIntegral())
                        .append(" where id = ")
                        .append(store.getId())
                        .append(";")
                        .append("\n");
                sql.append(sqlItem);
            }
            preparedStatement = dbConn.prepareStatement(sql.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
