package com.itheima.travel.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.dao.CategoryDao;
import com.itheima.travel.domain.Category;
import com.itheima.travel.service.CategoryService;
import com.itheima.travel.utils.JedisUtils;
import com.itheima.travel.utils.MyBatisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    //查询所有功能
    @Override
    public List<Category> findAll() {
        ObjectMapper objectMapper = new ObjectMapper();
        Jedis jedis=null;
        List<Category> list=null;

        try {
            //从连接池获取jedis对象
            jedis=JedisUtils.getJedis();
            //判断redis中是否有该数据
            if (jedis.exists("travel_category")) {
                //有则从redis中查询数据
                String travel_category = jedis.get("travel_category");
                //转换为java对象
                list=objectMapper.readValue(travel_category, List.class);
            } else {
                //没有数据则从数据库查询
                //获取sqlSession对象
                SqlSession sqlSession = MyBatisUtils.openSession();
                //获取代理对象
                CategoryDao categoryDao = sqlSession.getMapper(CategoryDao.class);

                //调用dao层查询所有功能从数据库获取数据,得到一个list集合
                list=categoryDao.findAll();
                //关闭sqlSession
                MyBatisUtils.close(sqlSession);
                //将list转为json对象
                String json = objectMapper.writeValueAsString(list);
                //存入到redis中
                jedis.set("travel_category", json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //归还连接
            jedis.close();
        }
        //返回结果
        return list;
    }
}
