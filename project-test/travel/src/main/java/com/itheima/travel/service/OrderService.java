package com.itheima.travel.service;

import com.itheima.travel.domain.Order;
import com.itheima.travel.domain.PageBean;
import com.itheima.travel.domain.ResultInfo;

import java.util.Map;

public interface OrderService {
    //保存功能
    void save(Order order);

    //查找功能
    Order findByOid(String oid);

    //更新支付状态功能
    void payNotify(Map<String, String> param);

    //判断支付状态功能
    ResultInfo isPay(String oid);

    //分页查询功能
    PageBean<Order> findByPage(Integer uid, Integer pageSize, Integer curPage, Integer state);
}
