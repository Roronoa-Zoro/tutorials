package com.illegalaccess.tutorials;

import com.illegalaccess.tutorials.ext.MixAll;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

//最简单的异步
public class TutorialTest1 {

    @Test
    public void test1() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        new Thread(new CompleteFutureTask(completableFuture)).start();
        String result = completableFuture.get();
        System.out.println(Thread.currentThread().getName() + " get result:" + result);
    }

    class CompleteFutureTask implements Runnable {
        CompletableFuture<String> completableFuture;

        public CompleteFutureTask(CompletableFuture<String> completableFuture) {
            this.completableFuture = completableFuture;
        }

        @Override
        public void run() {
            MixAll.simulateComputeCost();
            System.out.println(Thread.currentThread().getName() + " will complete future:" + completableFuture);
            completableFuture.complete("hello completableFuture");
        }
    }
}
