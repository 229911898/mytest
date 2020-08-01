package com.itheima.travel.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.travel.dao.UserDao;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.User;
import com.itheima.travel.service.UserService;
import com.itheima.travel.utils.MyBatisUtils;
import com.itheima.travel.utils.SmsUtils;
import org.apache.ibatis.session.SqlSession;

public class UserServiceImpl implements UserService {
    //注册功能
    @Override
    public ResultInfo register(User user) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        //调用dao层方法根据用户名查找获得一个user对象
        User user1 = userDao.findByUsername(user.getUsername());
        //判断对象是否存在
        if (user1 != null) {
            //存在说明已注册,返回一个resultInfo对象
            return new ResultInfo(false, "此用户已注册!");
        }

        //调用dao层方法根据手机号码查找获得一个user对象
        User user2 = userDao.finByTelephone(user.getTelephone());
        //判断对象是否存在
        if (user2 != null) {
            //存在说明已注册,返回一个resultInfo对象
            return new ResultInfo(false, "此手机号已使用!");
        }

        //调用工具类将密码进行加密
        String password = user.getPassword();
        password = SecureUtil.md5(password);
        user.setPassword(password);

        //调用dao层方法保存该对象
        userDao.save(user);
        //关闭seqSession
        MyBatisUtils.close(sqlSession);
        //返回一个resultInfo对象
        return new ResultInfo(true,"注册成功!");
    }

    //根据用户名查找功能
    @Override
    public ResultInfo findByUsername(String username) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        //调用dao层方法根据用户名查找获得一个user对象
        User user = userDao.findByUsername(username);
        //关闭seqSession
        MyBatisUtils.close(sqlSession);

        //判断对象是否存在
        if (user != null) {
            //存在,返回一个resultInfo对象
            return new ResultInfo(true);
        } else {
            //不存在,返回一个resultInfo对象
            return new ResultInfo(false);
        }
    }

    //根据手机号码查找功能
    @Override
    public ResultInfo findByTelephone(String telephone) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        //调用dao层方法根据手机号码查找获得一个user对象
        User user = userDao.finByTelephone(telephone);
        //关闭seqSession
        MyBatisUtils.close(sqlSession);

        //判断对象是否存在
        if (user != null) {
            //存在,返回一个resultInfo对象
            return new ResultInfo(true,"存在该用户",user);
        } else {
            //不存在,返回一个resultInfo对象
            return new ResultInfo(false);
        }
    }

    //发送短信验证码功能
    @Override
    public ResultInfo sendSmsCode(String telephone, String smsCode) {
        /*try {
            //指定签名
            String singName="黑马旅游";
            //指定模板
            String templateCode="SMS_195575683";
            //指定验证码参数
            String param = "{\"code\":"+smsCode+"}";
            //调用工具类
            SendSmsResponse sendSmsResponse = SmsUtils.sendSms(telephone, singName, templateCode, param);
            //判断验证码是否发送成功
            if ("OK".equals(sendSmsResponse.getCode())) {
                //发送成功,返回一个resultInfo对象
                return new ResultInfo(true,"发送成功");
            }
        } catch (ClientException e) {
            e.printStackTrace();
            //有异常,发送失败,返回一个resultInfo对象
            return new ResultInfo(false, "发送失败");
        }
        //发送失败,返回一个resultInfo对象
        return new ResultInfo(false, "发送失败");*/
        return new ResultInfo(true,"发送成功");
    }

    //用户名密码登陆功能
    @Override
    public ResultInfo pwdLogin(User user) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        //调用dao层方法根据用户名查找获得一个user对象
        User currentUser = userDao.findByUsername(user.getUsername());
        //关闭seqSession
        MyBatisUtils.close(sqlSession);

        //判断对象是否存在
        if (currentUser == null) {
            //不存在,返回一个resultInfo对象
            return new ResultInfo(false, "此用户不存在");
        }

        //调用工具类将密码进行加密
        String password = SecureUtil.md5(user.getPassword());
        //判断密码是否匹配
        if (!password.equals(currentUser.getPassword())) {
            //不匹配,返回一个resultInfo对象
            return new ResultInfo(false, "密码错误");
        }

        //用户名和密码都匹配,则登陆成功,返回一个resultInfo对象
        return new ResultInfo(true,"登陆成功",currentUser);
    }

    //根据uid查找功能
    @Override
    public User findByUid(int uid) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //调用dao层方法根据uid查找获得一个user对象
        User user = userDao.findByUid(uid);

        //关闭seqSession并返回usr对象
        MyBatisUtils.close(sqlSession);
        return user;
    }

    //更新个人信息功能
    @Override
    public void updateInfo(User user) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        //调用dao层将数据更新到数据库中
        userDao.update(user);
        //关闭seqSession
        MyBatisUtils.close(sqlSession);
    }
}
