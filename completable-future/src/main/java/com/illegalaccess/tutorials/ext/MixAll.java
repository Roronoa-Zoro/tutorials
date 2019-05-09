package com.illegalaccess.tutorials.ext;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MixAll {

    public static int simulateComputeCost() {
        Random r = new Random();
        int cost = r.nextInt(4);
        if (cost <=0) {
            cost = 3;
        }
        return simulateComputeCost(cost);
    }

    public static int simulateComputeCost(int cost) {
        try {
            //simulate fetching data from remote server
            TimeUnit.SECONDS.sleep(cost);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cost;
    }

    public static void printWithThread(String info) {
        System.out.println("at " + LocalDateTime.now() + ", thread:" + Thread.currentThread().getName() + info);
    }
}
