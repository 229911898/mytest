package com.itheima.travel.service;

import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.User;

public interface UserService {
    //注册功能
    ResultInfo register(User user);

    //根据用户名查找功能
    ResultInfo findByUsername(String username);

    //根据手机号码查找功能
    ResultInfo findByTelephone(String telephone);

    //发送短信验证码功能
    ResultInfo sendSmsCode(String telephone, String smsCode);

    //用户名密码登陆功能
    ResultInfo pwdLogin(User user);

    //根据uid查找功能
    User findByUid(int uid);

    //更新个人信息功能
    void updateInfo(User user);
}
