package com.itheima.travel.service.impl;

import com.itheima.travel.dao.OrderDao;
import com.itheima.travel.dao.OrderItemDao;
import com.itheima.travel.domain.Order;
import com.itheima.travel.domain.OrderItem;
import com.itheima.travel.domain.PageBean;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.service.OrderService;
import com.itheima.travel.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {
    //保存功能
    @Override
    public void save(Order order) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        OrderItemDao orderItemDao = sqlSession.getMapper(OrderItemDao.class);
        //调用orderDao保存订单
        orderDao.save(order);

        //遍历订单项集合,调用orderItemDao保存订单项
        for (OrderItem orderItem : order.getOrderItemList()) {
            orderItemDao.save(orderItem);
        }

        //关闭sqlSession
        MyBatisUtils.close(sqlSession);
    }

    //查找功能
    @Override
    public Order findByOid(String oid) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        //调用dao层查询功能获取到order对象
        Order order=orderDao.findByOid(oid);
        //关闭sqlSession并返回结果
        MyBatisUtils.close(sqlSession);
        return order;
    }

    //更新支付状态功能
    @Override
    public void payNotify(Map<String, String> map) {
        //判断支付状态
        if ("FAIL".equals(map.get("result_code"))) {
            //支付失败抛异常
            throw new RuntimeException("微信支付失败...");
        }

        //支付成功,获取参数中的订单号
        String oid = map.get("out_trade_no");
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        OrderItemDao orderItemDao = sqlSession.getMapper(OrderItemDao.class);
        //调用dao层更新订单和订单项支付状态
        orderDao.updateState(oid);
        orderItemDao.updateState(oid);
        //关闭sqlSession
        MyBatisUtils.close(sqlSession);
    }

    //判断支付状态功能
    @Override
    public ResultInfo isPay(String oid) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        //调用dao层查询功能获取到order对象
        Order order=orderDao.findByOid(oid);
        //关闭sqlSession
        MyBatisUtils.close(sqlSession);

        //判断支付状态并封装到resultInfo中
        if (order.getState() == 1) {
            //说明已支付,返回结果为true
            return new  ResultInfo(true,"已支付");
        } else {
            //未支付,返回结果为false
            return new ResultInfo(false,"未支付");
        }
    }

    //分页查询功能
    @Override
    public PageBean<Order> findByPage(Integer uid, Integer pageSize, Integer curPage, Integer state) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        OrderDao orderDao = sqlSession.getMapper(OrderDao.class);
        //调用dao层根据uid和state查询总记录数
        Integer totalCount=orderDao.findCount(uid,state);

        //计算总页数
        Integer totalPage = (totalCount % pageSize) == 0 ? (totalCount / pageSize) : (totalCount / pageSize) + 1;
        //计算分页查询开始索引
        Integer startIndex = (curPage - 1) * pageSize;
        //调用dao层分页查询功能,获取分页数据
        List<Order> list=orderDao.findByPage(startIndex,pageSize,uid,state);

        //创建pageBean对象并封装参数
        PageBean<Order> pageBean = new PageBean<>();
        pageBean.setPageSize(pageSize);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setCurPage(curPage);
        pageBean.setList(list);

        //关闭sqlSession并返回结果
        MyBatisUtils.close(sqlSession);
        return pageBean;
    }
}
