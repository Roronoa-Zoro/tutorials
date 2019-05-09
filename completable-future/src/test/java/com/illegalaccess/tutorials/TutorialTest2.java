package com.illegalaccess.tutorials;

import com.illegalaccess.tutorials.ext.MixAll;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 异步计算 测试
 */

public class TutorialTest2 {

    /**
     * runAsync()进行，没有返回结果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void runAsyncTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            MixAll.simulateComputeCost();
            System.out.println(Thread.currentThread().getName() + " runAsyncTest...................");
        });
//        cf.get();
        MixAll.simulateComputeCost(8);
    }

    /**
     * supplyAsync 带返回结果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void supplyAsyncTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            MixAll.simulateComputeCost();
            MixAll.printWithThread(" will return something");
            return "hello supplyAsync";
        });

        String data = cf.get();
        MixAll.printWithThread(" get data:" + data);
    }
}
