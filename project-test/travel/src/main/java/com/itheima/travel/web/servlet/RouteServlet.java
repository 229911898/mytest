package com.itheima.travel.web.servlet;

import com.itheima.travel.domain.PageBean;
import com.itheima.travel.domain.Route;
import com.itheima.travel.service.RouteService;
import com.itheima.travel.utils.BeanFactory;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/routeServlet")
public class RouteServlet extends BaseServlet {
    RouteService routeService = (RouteService) BeanFactory.getBean("routeService");

    //分页查询功能
    public void findByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求参数
        String pageSizeStr = request.getParameter("pageSize");
        String curPageStr = request.getParameter("curPage");
        String cid = request.getParameter("cid");
        String rname = request.getParameter("rname");
        //如果pageSize参数为空,则默认为10
        if (StringUtils.isEmpty(pageSizeStr)) {
            pageSizeStr = "10";
        }

        //如果curPage参数为空,则默认为1(查询第一页数据)
        if (StringUtils.isEmpty(curPageStr)) {
            curPageStr = "1";
        }

        //数据类型转换
        Integer pageSize = Integer.parseInt(pageSizeStr);
        Integer curPage = Integer.parseInt(curPageStr);
        //调用service层分页查询功能,返回一个pageBean对象
        PageBean<Route> pageBean = routeService.findByPage(pageSize, curPage,cid,rname);

        //将相关数据存入request域中
        request.setAttribute("pb", pageBean);
        request.setAttribute("cid",cid);
        request.setAttribute("rname",rname);
        //转发到旅游路线分页数据展示页面
        request.getRequestDispatcher("route_list.jsp").forward(request,response);
    }

    //查询详情功能
    public void findByDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求参数
        String rid = request.getParameter("rid");
        //调用service层查询详情功能,得到一个route对象
        Route route=routeService.findByDetail(rid);
        //将结果存入request域中
        request.setAttribute("route",route);
        //转发到线路详情展示页面
        request.getRequestDispatcher("route_detail.jsp").forward(request,response);

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }
}
