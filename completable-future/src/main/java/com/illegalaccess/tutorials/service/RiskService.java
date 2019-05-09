package com.illegalaccess.tutorials.service;

import com.illegalaccess.tutorials.ext.MixAll;

import java.util.Random;

//风险定价服务
public enum RiskService {

    Instance;

    public int getRiskScoreFromA4Product() {
        Random r = new Random();
        MixAll.simulateComputeCost();
        int score = r.nextInt(10);
        MixAll.printWithThread(" getRiskScoreFromA4Product return score=" + score);
        return score;
    }

    public int getRiskScoreFromB4Product() {
        Random r = new Random();
        MixAll.simulateComputeCost();
        int score = r.nextInt(13);
        MixAll.printWithThread(" getRiskScoreFromB4Product return score=" + score);
        return score;
    }
}
