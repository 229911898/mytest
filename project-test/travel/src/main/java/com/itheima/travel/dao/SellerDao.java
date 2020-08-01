package com.itheima.travel.dao;

import com.itheima.travel.domain.Seller;

public interface SellerDao {
    //根据sid查找功能
    Seller findBySid(Integer sid);
}
