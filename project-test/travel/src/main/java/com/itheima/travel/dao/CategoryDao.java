package com.itheima.travel.dao;

import com.itheima.travel.domain.Category;

import java.util.List;

public interface CategoryDao {
    //查询所有功能
    List<Category> findAll();

    //根据cid查询功能
    Category findByCid(Integer cid);
}
