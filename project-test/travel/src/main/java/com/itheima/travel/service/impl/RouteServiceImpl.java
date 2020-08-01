package com.itheima.travel.service.impl;

import com.itheima.travel.dao.RouteDao;
import com.itheima.travel.domain.PageBean;
import com.itheima.travel.domain.Route;
import com.itheima.travel.service.RouteService;
import com.itheima.travel.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    //分页查询功能
    @Override
    public PageBean<Route> findByPage(Integer pageSize, Integer curPage, String cid, String rname) {
        //获取seqSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        RouteDao routeDao = sqlSession.getMapper(RouteDao.class);
        //调用dao层根据cid和rname查询所有记录数
        Integer totalCount = routeDao.findCount(cid, rname);

        //计算总页数
        Integer totalPage = (totalCount % pageSize) == 0 ? (totalCount / pageSize) : (totalCount / pageSize) + 1;
        //计算分页查询开始索引
        Integer startIndex = (curPage - 1) * pageSize;

        //调用dao层分页查询功能,获取分页数据
        List<Route> list = routeDao.findByPage(startIndex, pageSize, cid, rname);
        //创建pageBean对象并封装参数
        PageBean<Route> pageBean = new PageBean<>();
        pageBean.setPageSize(pageSize);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setCurPage(curPage);
        pageBean.setList(list);

        //关闭seqSession并返回结果
        MyBatisUtils.close(sqlSession);
        return pageBean;
    }

    //查询详情功能
    @Override
    public Route findByDetail(String rid) {
        //获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.openSession();
        //获取代理对象
        RouteDao routeDao = sqlSession.getMapper(RouteDao.class);
        //调用dao层查询详情功能,获得route对象
        Route route=routeDao.findByDetail(rid);
        //关闭sqlSession并返回结果
        MyBatisUtils.close(sqlSession);
        return route;
    }
}
