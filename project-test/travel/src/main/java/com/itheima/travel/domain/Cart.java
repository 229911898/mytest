package com.itheima.travel.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * 购物车实体类
 */
@Data
public class Cart implements Serializable {
    private Integer cartNum;//总数量
    private Double cartTotal;//总金额
    private LinkedHashMap<String,CartItem> cartItemMap=new LinkedHashMap<>();//购物项集合,key为sid,value为购物项

    //重写get总数量方法
    public Integer getCartNum() {
        //重置总数量
        cartNum=0;
        for (CartItem cartItem : cartItemMap.values()) {
            cartNum+=cartItem.getNum();
        }
        return cartNum;
    }

    //重写get总金额方法
    public Double getCartTotal() {
        //重置总金额
        cartTotal=0.0;
        for (CartItem cartItem : cartItemMap.values()) {
            cartTotal+=cartItem.getItemTotal();
        }
        return cartTotal;
    }
}
