package com.illegalaccess.tutorials.bo;

import lombok.Data;

import java.io.Serializable;
//商户信息
@Data
public class Merchant implements Serializable {

    private static final long serialVersionUID = -3816149872116234611L;
    private int id;
    private String merchantName;
    private String merchantOwner;
}
