package com.illegalaccess.tutorials;

import com.illegalaccess.tutorials.ext.MixAll;
import com.illegalaccess.tutorials.service.ProductService;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture更多操作
 * thenApply()
 * thenAccept()
 * thenRun()
 */
public class TutorialTest3 {

    @Test
    public void thenApplyTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf = CompletableFuture
                .supplyAsync(() -> {
                    MixAll.simulateComputeCost();
                    MixAll.printWithThread(" will return something");
                    return "hello supplyAsync";
                })
                .thenApply(info -> {
                    MixAll.simulateComputeCost();
                    MixAll.printWithThread(" thenApply will return something");
                    return "thenApply|" + info;
                });


        String data = cf.get();
        MixAll.printWithThread(" get>>" + data);
    }

    /**
     * thenApplyAsync 测试， 应该thenApplyAsync是在另外的线程执行
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenApplyAsyncTest() throws ExecutionException, InterruptedException {
        for (int i = 0; i <=6; i++) {
            final int idx = i;
            CompletableFuture<String> cf = CompletableFuture
                    .supplyAsync(() -> {
                        MixAll.simulateComputeCost();
                        MixAll.printWithThread("|" + idx + " will return something");
                        return "hello supplyAsync";
                    })
                    .thenApplyAsync(info -> {
                        MixAll.simulateComputeCost();
                        MixAll.printWithThread("|" + idx + " thenApplyAsync will return something");
                        return "thenApplyAsync|" + info;
                    });
            MixAll.printWithThread(" loop>>" + i + " complete");
        }

        MixAll.simulateComputeCost(28);

//        String data = cf.get();
//        MixAll.printWithThread(" get>>" + data);
    }

    /**
     * thenAccept  主要是对产生的数据进行消费，无返回值
     */
    @Test
    public void thenAcceptTest() {
        CompletableFuture.supplyAsync(() -> ProductService.Instance.getProduct(12))
                .thenAccept(product -> MixAll.printWithThread(" thenAccept get productName:" + product.getName()));

        //等待执行完成
        MixAll.simulateComputeCost(8);
    }

    /**
     * thenAcceptBoth 又接收一个CompletableFuture 参加最后的计算而已
     */
    @Test
    public void thenAcceptBothTest() {
        CompletableFuture.supplyAsync(() -> ProductService.Instance.getProduct(12))
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> ProductService.Instance.getProduct(15)),
                        (p1, p2) -> {
                            MixAll.printWithThread(" thenAcceptBoth" + p1.getId() + "|" + p2.getId());
                        });

        //等待执行完成
        MixAll.simulateComputeCost(5);
    }

    /**
     * acceptEither 先执行完的被返回， 但是2个任务都执行了
     */
    @Test
    public void thenAcceptEitherTest() {
        CompletableFuture.supplyAsync(() -> ProductService.Instance.getProduct(12))
                .acceptEither(CompletableFuture.supplyAsync(() -> ProductService.Instance.getProduct(15)),
                        p -> MixAll.printWithThread(" product:" + p.getName() + " returned..."));

        //等待执行完成
        MixAll.simulateComputeCost(5);
    }

    /**
     * thenRun
     */
    @Test
    public void thenRunTest() {
        long start = System.currentTimeMillis();
        CompletableFuture
                .runAsync(() -> ProductService.Instance.getProduct(12))
                .thenRun(() -> MixAll.printWithThread(" thenRun cost:" + (System.currentTimeMillis() - start) + "ms"));
        //等待执行完成
        MixAll.simulateComputeCost(8);
    }
}
