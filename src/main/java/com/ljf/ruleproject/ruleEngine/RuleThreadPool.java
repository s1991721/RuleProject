package com.ljf.ruleproject.ruleEngine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mr.lin on 2020/6/24
 */
public class RuleThreadPool {

    private static ExecutorService executorService= Executors.newCachedThreadPool();

    public static void submit(Runnable runnable){
        executorService.submit(runnable);
    }

}
