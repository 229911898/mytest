package com.itheima.travel.dao;

import com.itheima.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    //根据rid查找功能
    List<RouteImg> findByRid(Integer rid);
}
