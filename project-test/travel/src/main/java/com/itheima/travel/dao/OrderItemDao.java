package com.itheima.travel.dao;

import com.itheima.travel.domain.OrderItem;

import java.util.List;

public interface OrderItemDao {
    //保存订单项功能
    void save(OrderItem orderItem);

    //更新支付状态功能
    void updateState(String oid);

    //根据oid查询功能
    List<OrderItem> findByOid(String oid);
}
