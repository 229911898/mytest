package com.itheima.travel.dao;

import com.itheima.travel.domain.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteDao {
    //根据cid和rname查询总记录数功能
    int findCount(@Param("cid") String cid, @Param("rname") String rname);

    //分页查询功能
    List<Route> findByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize, @Param("cid") String cid,@Param("rname") String rname);

    //查询详情功能
    Route findByDetail(String rid);

    //根据rid查询功能
    Route findByRid(Integer rid);
}
