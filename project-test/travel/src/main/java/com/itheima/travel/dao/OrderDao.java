package com.itheima.travel.dao;

import com.itheima.travel.domain.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDao {
    //保存功能
    void save(Order order);

    //查找功能
    Order findByOid(String oid);

    //更新支付状态功能
    void updateState(String oid);

    //根据uid和state查询总记录数
    Integer findCount(@Param("uid") Integer uid, @Param("state") Integer state);

    //分页查询功能
    List<Order> findByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize, @Param("uid")Integer uid, @Param("state")Integer state);
}
