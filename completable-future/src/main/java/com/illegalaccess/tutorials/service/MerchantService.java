package com.illegalaccess.tutorials.service;

import com.illegalaccess.tutorials.bo.Merchant;
import com.illegalaccess.tutorials.bo.Product;
import com.illegalaccess.tutorials.ext.MixAll;

import java.util.UUID;

public enum MerchantService {

    Instance;

    public Merchant getMerchant(Product product) {
        MixAll.printWithThread(" fetch merchant:" + product.getMerchantId());
        MixAll.simulateComputeCost();
        Merchant m = new Merchant();
        m.setId(product.getMerchantId());
        m.setMerchantName(UUID.randomUUID().toString());
        m.setMerchantOwner(UUID.randomUUID().toString());
        return m;
    }
}
