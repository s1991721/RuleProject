package com.ljf.ruleproject.ruleEngine;

import com.ljf.ruleproject.entity.RuleInfo;
import com.ljf.ruleproject.poet.Store;
import com.ljf.ruleproject.service.DataService;
import com.ljf.ruleproject.ws.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.event.process.*;
import org.kie.api.event.rule.*;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.util.List;

/**
 * Created by mr.lin on 2020/6/24
 */
@Slf4j
public class RuleExecutor implements Runnable {

    private RuleInfo ruleInfo;
    private DataService dataService;

    public RuleExecutor(RuleInfo ruleInfo, DataService dataService) {
        this.ruleInfo = ruleInfo;
        this.dataService = dataService;
    }

    private void sendInfo(String msg) {
        log.info(msg);
        WebSocketServer.sendInfo(msg, String.valueOf(ruleInfo.getId()));
    }

    @Override
    public void run() {

        sendInfo("规则开始执行");
        String rule = ruleInfo.getRule();

        sendInfo("获取到规则:");
        sendInfo(rule);
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

        List<Store> datas = dataService.getData();

        sendInfo("获取到数据，开始插入数据");
        for (Object data : datas) {
            ksession.insert(data);
            sendInfo(data.toString());
        }

        sendInfo("插入数据完成");
        sendInfo("执行规则开始");
        ksession.fireAllRules();

        sendInfo("执行规则结束");
        ksession.dispose();

        sendInfo("结果存库开始");
        dataService.saveData(datas);

        sendInfo("结果存库结束");
    }

}
