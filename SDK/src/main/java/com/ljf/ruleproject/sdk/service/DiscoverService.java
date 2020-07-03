package com.ljf.ruleproject.sdk.service;

import com.ljf.ruleproject.sdk.bean.Application;
import com.ljf.ruleproject.sdk.bean.Instance;
import com.ljf.ruleproject.sdk.util.HttpClient;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.lin on 2020/7/3
 */
public class DiscoverService {

    public static Application getServer(String serverName) {
        List<Application> applicationList;
        try {
            applicationList = getAllServers();
        } catch (Exception e) {
            System.out.println("服务不可用");
            e.printStackTrace();
            return null;
        }
        for (Application application : applicationList) {
            application.getName().equals(serverName);
            return application;
        }
        return null;
    }

    /**
     * 获取所有服务
     *
     * @return
     * @throws JSONException
     * @throws IOException
     */
    private static List<Application> getAllServers() throws JSONException, IOException {
        JSONObject jsonObject = XML.toJSONObject(getXML());

        System.out.println(jsonObject.toString());

        JSONObject applications = jsonObject.getJSONObject("applications");
        System.out.println("applications : " + applications.toString());

        String application = applications.getString("application");
        System.out.println("application 外 : " + application);

        List<Application> applicationList = new ArrayList<>();

        if (application.startsWith("[")) {//多服务
            JSONArray jsonArray = new JSONArray(application);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                applicationList.add(jsonToApplication(jsonObject1));
            }

        } else {
            applicationList.add(jsonToApplication(new JSONObject(application)));
        }

        return applicationList;

    }

    /**
     * 获取服务列表xml
     *
     * @return
     * @throws IOException
     */
    private static String getXML() throws IOException {
        Request request = new Request.Builder().url("http://localhost:8080/eureka/apps/").get().build();
        Call call = HttpClient.okHttpClient.newCall(request);
        Response response = call.execute();
        String xml = response.body().string();
        System.out.println(xml);
        if (response.isSuccessful()) {
            return xml;
        } else {
            throw new RuntimeException("服务中心不可用");
        }
    }

    /**
     * 服务列表xml转json
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    private static Application jsonToApplication(JSONObject jsonObject) throws JSONException {
        System.out.println("application 内: " + jsonObject.toString());
        String instance = jsonObject.getString("instance");

        Application application = new Application();
        application.setName(jsonObject.getString("name"));

        List<Instance> instanceList = new ArrayList<>();

        if (instance.startsWith("[")) {//多实例
            JSONArray jsonArray = new JSONArray(instance);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                instanceList.add(jsonToInstance(jsonObject1));
            }
        } else {
            instanceList.add(jsonToInstance(new JSONObject(instance)));
        }

        application.setInstance(instanceList);

        return application;
    }

    private static Instance jsonToInstance(JSONObject jsonObject) throws JSONException {
        System.out.println("instance : " + jsonObject.toString());

        Instance instance = new Instance();
        instance.setApp(jsonObject.getString("app"));
        instance.setIpAddr(jsonObject.getString("ipAddr"));
        instance.setPort(jsonObject.getJSONObject("port").getString("content"));
        return instance;
    }

}
