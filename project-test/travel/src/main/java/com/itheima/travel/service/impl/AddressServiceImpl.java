package com.itheima.travel.service.impl;

import com.itheima.travel.dao.AddressDao;
import com.itheima.travel.domain.Address;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.service.AddressService;
import com.itheima.travel.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import java.util.List;

public class AddressServiceImpl implements AddressService {
    //根据uid查询所有功能
    @Override
    public List<Address> findAllByUid(int uid) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        AddressDao addressDao = sqlSession.getMapper(AddressDao.class);

        //调用dao层根据uid查询所有功能获得一个list集合
        List<Address> list=addressDao.findAllByUid(uid);
        //关闭seqSession并返回结果
        MyBatisUtils.close(sqlSession);
        return list;
    }

    //保存功能
    @Override
    public void save(Address address) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        AddressDao addressDao = sqlSession.getMapper(AddressDao.class);

        //调用dao层保存数据到数据库
        addressDao.save(address);
        //关闭seqSession
        MyBatisUtils.close(sqlSession);
    }

    //根据aid查询功能
    @Override
    public Address findByAid(int aid) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        AddressDao addressDao = sqlSession.getMapper(AddressDao.class);

        //调用dao层根据aid查询功能,获得address对象
        Address address=addressDao.findByAid(aid);
        //关闭seqSession并返回结果
        MyBatisUtils.close(sqlSession);
        return address;
    }

    //删除功能
    @Override
    public void delete(int aid) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        AddressDao addressDao = sqlSession.getMapper(AddressDao.class);

        //调用dao层删除功能
        addressDao.delete(aid);
        //关闭seqSession并返回结果
        MyBatisUtils.close(sqlSession);
    }

    //更新功能
    @Override
    public ResultInfo update(Address address) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        AddressDao addressDao = sqlSession.getMapper(AddressDao.class);

        //调用dao层更新功能
        addressDao.update(address);
        //关闭seqSession并响应一个结果集
        MyBatisUtils.close(sqlSession);
        return new ResultInfo(true, "设置成功");
    }
}
