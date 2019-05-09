package com.illegalaccess.tutorials;

import com.illegalaccess.tutorials.bo.Merchant;
import com.illegalaccess.tutorials.bo.Product;
import com.illegalaccess.tutorials.ext.MixAll;
import com.illegalaccess.tutorials.service.MerchantService;
import com.illegalaccess.tutorials.service.ProductService;
import com.illegalaccess.tutorials.service.RiskService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class TutorialTest4 {

    /**
     * 合并2个有依赖关系的 CompletableFuture
     */
    @Test
    public void combineFutureTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Merchant> cf = CompletableFuture
                .supplyAsync(() -> ProductService.Instance.getProduct(12)) //step1
                .thenCompose(product -> CompletableFuture.supplyAsync(() -> MerchantService.Instance.getMerchant(product))); //step2

        cf.get();
    }

    /**
     * 合并2个有独立的 CompletableFuture
     */
    @Test
    public void combineFutureTest2() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> cf = CompletableFuture
                .supplyAsync(() -> RiskService.Instance.getRiskScoreFromA4Product())
                .thenCombine(CompletableFuture.supplyAsync(() -> RiskService.Instance.getRiskScoreFromB4Product()),
                        (ascore, bscore) -> (ascore + bscore) / 2);

        MixAll.printWithThread(" get score=" + cf.get());
    }


    /**
     * 组装所有 CompletableFuture
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void combineAllFutureTest() throws ExecutionException, InterruptedException {
        List<Integer> list = new ArrayList<>(10);
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            list.add(i + r.nextInt(10));
        }
        //创建10个异步获取产品的任务
        List<CompletableFuture<Product>> proFutures = list.stream()
                .map(in -> CompletableFuture.supplyAsync(() -> ProductService.Instance.getProduct(in)))
                .collect(Collectors.toList());


        //组装到一起
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(proFutures.toArray(new CompletableFuture[proFutures.size()]));

//        // 获取最终数据 方式1
//        CompletableFuture<List<Product>> productList = allFutures.thenApply(v -> {
//            MixAll.printWithThread(" thenApply collect all product info");
//            return proFutures.stream().map(cfp -> cfp.join()).collect(Collectors.toList());
//        });
//
//        List<Product> data = productList.get();
//        MixAll.printWithThread(" productInfo:" + data);

        // 获取最终数据 方式2
        final List<Product> data2 = new ArrayList<>(proFutures.size());
        allFutures.thenRun(() -> {
            MixAll.printWithThread(" thenRun collect all product info");
            data2.addAll(proFutures.stream().map(cfp -> cfp.join()).collect(Collectors.toList()));
        });
        allFutures.get();
        MixAll.printWithThread(" productInfo:" + data2);

        //获取最终数据 方式3, 跟上面类似 , 略
        //allFutures.thenAccept()
    }

    /**
     * 组装所有Future  等待任意一个完成
     */
    @Test
    public void combineAnyFutureTest() throws ExecutionException, InterruptedException {
        List<Integer> list = new ArrayList<>(10);
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            list.add(i + r.nextInt(10));
        }
        //创建10个异步获取产品的任务
        List<CompletableFuture<Integer>> scoreFutures = list.stream()
                .map(in -> CompletableFuture.supplyAsync(() -> RiskService.Instance.getRiskScoreFromB4Product()))
                .collect(Collectors.toList());

        CompletableFuture<Object> cfAny = CompletableFuture.anyOf(scoreFutures.toArray(new CompletableFuture[scoreFutures.size()]));
        Integer score = (Integer) cfAny.get();
        MixAll.printWithThread(" get score=" + score);
    }

    /**
     * 异常处理 exceptionally
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void exceptionallyTest() throws ExecutionException, InterruptedException {
        final int idx = 2;
        CompletableFuture<String> cf = CompletableFuture
                .supplyAsync(() -> {
                    MixAll.printWithThread(" in supplyAsync....");
                    if (idx % 2 == 0) {
                        throw new IllegalArgumentException("test illegal argument exception");
                    }
                    return UUID.randomUUID().toString();
                })
                .thenApply(first -> {
                    if (idx % 2 == 1) {
                        throw new IllegalArgumentException("test illegal argument exception");
                    }
                    MixAll.printWithThread(" in first thenApply....");
                    return "thenApply1-" + first;
                })
                .thenApply(second -> {
                    MixAll.printWithThread(" in second thenApply....");
                    return "thenApply2-" + second;
                })
                .exceptionally(ex -> {
                    MixAll.printWithThread(" in exceptionally....");
                    return "exceptionally";
                });

        MixAll.printWithThread(" get data:" + cf.get());
    }


    @Test
    public void exceptionHandleTest() throws ExecutionException, InterruptedException {
        final int idx = 1;
        CompletableFuture<String> cf = CompletableFuture
                .supplyAsync(() -> {
                    MixAll.printWithThread(" in supplyAsync....");
                    if (idx % 2 == 0) {
                        throw new IllegalArgumentException("test illegal argument exception");
                    }
                    return UUID.randomUUID().toString();
                })
                .thenApply(first -> {
                    if (idx % 2 == 1) {
                        throw new IllegalArgumentException("test illegal argument exception");
                    }
                    MixAll.printWithThread(" in first thenApply....");
                    return "thenApply1-" + first;
                })
                .thenApply(second -> {
                    MixAll.printWithThread(" in second thenApply....");
                    return "thenApply2-" + second;
                })
                .handle((info, ex) -> {
                    MixAll.printWithThread(" in exception handle,info=" + info + ",ex=" + ex);
                    if (ex != null) {
                        return "exception handle";
                    }
                    return info;
                });

        MixAll.printWithThread(" get data:" + cf.get());
    }
}
