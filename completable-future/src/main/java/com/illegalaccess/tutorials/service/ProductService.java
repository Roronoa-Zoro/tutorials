package com.illegalaccess.tutorials.service;

import com.illegalaccess.tutorials.bo.Product;
import com.illegalaccess.tutorials.ext.MixAll;
import java.util.UUID;

public enum ProductService {

    Instance;

    public Product getProduct(int productId) {

        int cost = MixAll.simulateComputeCost();
        Product u = new Product();
        u.setId(productId);
        u.setPrice(cost);
        u.setMerchantId(productId + cost);
        u.setName(Thread.currentThread().getName());
        MixAll.printWithThread(" fetch product:" + productId);
        return u;
    }
}
