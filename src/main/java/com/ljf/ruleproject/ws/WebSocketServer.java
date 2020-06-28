package com.ljf.ruleproject.ws;

import com.ljf.ruleproject.entity.DBInfo;
import com.ljf.ruleproject.entity.RuleInfo;
import com.ljf.ruleproject.ruleEngine.RuleExecutor;
import com.ljf.ruleproject.ruleEngine.RuleThreadPool;
import com.ljf.ruleproject.service.impl.RuleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mr.lin on 2020/6/28
 */
@ServerEndpoint("/imserver/{userId}")
@Component
@Slf4j
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
            //加入set中
        } else {
            webSocketMap.put(userId, this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + userId + ",报文:" + message);

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
        RuleThreadPool.submit(new RuleExecutor(ruleInfo1));
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) {
        try {
            log.info("发送消息到:" + userId + "，报文:" + message);
            if (!StringUtils.isEmpty(userId) && webSocketMap.containsKey(userId)) {
                webSocketMap.get(userId).sendMessage(message);
            } else {
                log.error("用户" + userId + ",不在线！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
