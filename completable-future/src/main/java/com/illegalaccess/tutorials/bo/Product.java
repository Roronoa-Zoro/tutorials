package com.illegalaccess.tutorials.bo;

import lombok.Data;

import java.io.Serializable;
// 产品信息, 产品隶属于一个商户下面
@Data
public class Product implements Serializable {

    private static final long serialVersionUID = 9062246887965261985L;

    private int id;
    private int merchantId;
    private String name;
    private int price;
}
