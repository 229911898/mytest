package com.itheima.travel.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.domain.Cart;
import com.itheima.travel.domain.User;
import redis.clients.jedis.Jedis;
import java.io.IOException;
/**
 * 购物车redis工具类
 */
public class CartUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    //将用户购物车缓存到redis中
    public static void setCartToRedis(User user, Cart cart) {
        try {
            //获取jedis对象
            Jedis jedis = JedisUtils.getJedis();
            //将cart转为json
            String json = objectMapper.writeValueAsString(cart);
            //存入到redis中
            jedis.set("cart_" + user.getUsername(), json);
            //归还连接
            jedis.close();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    //从redis缓存中获取用户购物车
    public static Cart getCartFromRedis(User user) {
        //获取jedis对象
        Jedis jedis = JedisUtils.getJedis();
        Cart cart =null;
        //判断该user的redis中是否有购物车
        if (jedis.exists("cart_" + user.getUsername())) {
            //有则获取数据
            String json = jedis.get("cart_" + user.getUsername());
            try {
                //转为cart对象
                cart = objectMapper.readValue(json, Cart.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //没有则为该用户创建购物车
            cart=new Cart();
        }

        //归还连接
        jedis.close();
        return cart;
    }
}
