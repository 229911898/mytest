package com.itheima.travel.dao;

import com.itheima.travel.domain.User;

public interface UserDao {
    //根据用户名查找功能
    User findByUsername(String username);

    //根据手机号码查找功能
    User finByTelephone(String telephone);

    //保存功能
    void save(User user);

    //根据uid查找功能
    User findByUid(int uid);

    //更新个人信息功能
    void update(User user);
}
