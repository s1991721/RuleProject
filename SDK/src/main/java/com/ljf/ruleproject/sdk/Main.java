package com.ljf.ruleproject.sdk;

import com.ljf.ruleproject.sdk.bean.Application;
import com.ljf.ruleproject.sdk.bean.Instance;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.dom4j.DocumentException;
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
public class Main {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    public static String getXML() throws IOException {
        Request request = new Request.Builder().url("http://localhost:8080/eureka/apps/").get().build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        String xml = response.body().string();
        System.out.println(xml);
        if (response.isSuccessful()) {

        }else {

        }
        return xml;
    }

    public static void main(String[] args) throws IOException, DocumentException, JSONException {

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


        System.out.println(applicationList.toString());

//        System.out.println(application.getString("name"));
//        String ip=application.getJSONObject("instance").getString("ipAddr");
//        System.out.println(ip);
//        String port=application.getJSONObject("instance").getJSONObject("port").getString("content");
//        System.out.println(port);
//
//        String url=ip+":"+port+"/realExecute/{id}";
//        System.out.println(url);

    }

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

    public static Instance jsonToInstance(JSONObject jsonObject) throws JSONException {
        System.out.println("instance : " + jsonObject.toString());

        Instance instance = new Instance();
        instance.setApp(jsonObject.getString("app"));
        instance.setIpAddr(jsonObject.getString("ipAddr"));
        instance.setPort(jsonObject.getJSONObject("port").getString("content"));
        return instance;
    }

}
