package com.itheima.travel.domain;

import lombok.Data;

import java.io.Serializable;
/**
 * 购物项实体类
 */
@Data
public class CartItem implements Serializable {
    private Route route;//线路
    private Integer num;//数量
    private Double itemTotal;//小计

    //重写小计方法
    public Double getItemTotal() {
        return route.getPrice()*num;
    }
}
