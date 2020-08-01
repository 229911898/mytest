package com.itheima.travel.dao;

import com.itheima.travel.domain.Address;

import java.util.List;

public interface AddressDao {
    //根据uid查询所有功能
    List<Address> findAllByUid(int uid);

    //保存功能
    void save(Address address);

    //根据aid查询功能
    Address findByAid(int aid);

    //删除功能
    void delete(int aid);

    //更新功能
    void update(Address address);
}
