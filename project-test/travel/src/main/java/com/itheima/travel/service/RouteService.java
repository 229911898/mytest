package com.itheima.travel.service;

import com.itheima.travel.domain.PageBean;
import com.itheima.travel.domain.Route;

public interface RouteService {
    //分页查询功能
    PageBean<Route> findByPage(Integer pageSize, Integer curPage, String cid,String rname);

    //查询详情功能
    Route findByDetail(String rid);
}
