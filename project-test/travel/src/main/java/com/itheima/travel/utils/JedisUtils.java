package com.itheima.travel.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.util.ResourceBundle;
/*
*   jedis工具类
* */
public class JedisUtils {
    private static JedisPool jedisPool;//连接池

    private static String host;//主机地址
    private static Integer port;//端口号
    private static Integer maxTotal;//最大连接数
    private static Integer maxIdle;//最大空闲连接数

    static {
        //读取配置文件.获取参数
        ResourceBundle bundle = ResourceBundle.getBundle("jedis");
        host = bundle.getString("jedis.host");
        port = Integer.parseInt(bundle.getString("jedis.port"));
        maxTotal = Integer.parseInt(bundle.getString("jedis.maxTotal"));
        maxIdle = Integer.parseInt(bundle.getString("jedis.maxIdle"));

        //创建连接池配置对象,设置配置参数
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);

        //创建连接池对象
        jedisPool = new JedisPool(jedisPoolConfig, host, port);
    }

    //获取jedis连接的静态方法
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
