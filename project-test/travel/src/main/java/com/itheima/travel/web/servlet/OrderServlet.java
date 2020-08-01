package com.itheima.travel.web.servlet;

import cn.hutool.core.util.IdUtil;
import com.itheima.travel.domain.*;
import com.itheima.travel.service.AddressService;
import com.itheima.travel.service.OrderService;
import com.itheima.travel.utils.BeanFactory;
import com.itheima.travel.utils.CartUtils;
import com.itheima.travel.utils.JedisUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@WebServlet("/orderServlet")
public class OrderServlet extends BaseServlet {
    AddressService addressService = (AddressService) BeanFactory.getBean("addressService");
    OrderService orderService = (OrderService) BeanFactory.getBean("orderService");

    //结算功能
    public void preOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User user = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (user == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        //调用addressService根据uid查询所有功能,获取用户地址的list集合
        List<Address> list = addressService.findAllByUid(user.getUid());
        //获取当前用户的cart对象
        Cart cart = CartUtils.getCartFromRedis(user);
        //将数据存入request域中
        request.setAttribute("list",list);
        request.setAttribute("cart",cart);
        //转发到order_info.jsp页面展示数据
        request.getRequestDispatcher("order_info.jsp").forward(request,response);

    }

    //提交功能
    public void subOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User user = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (user == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        //已登陆,获取请求参数中的aid
        String addressId = request.getParameter("addressId");
        //转换成int类型
        int aid = Integer.parseInt(addressId);
        //调用addressService根据aid查询功能,获取用户提交的收货地址
        Address address = addressService.findByAid(aid);
        //获取当前用户的cart对象
        Cart cart = CartUtils.getCartFromRedis(user);

        //创建订单对象并封装参数
        Order order = new Order();
        //随机生成订单id
        order.setOid(IdUtil.simpleUUID());
        //下单时间
        order.setOrdertime(new Date());
        //订单金额
        order.setTotal(cart.getCartTotal());
        //支付状态,未支付标记为0
        order.setState(0);
        //订单收货地址,联系人和手机号码
        order.setAddress(address.getAddress());
        order.setContact(address.getContact());
        order.setTelephone(address.getTelephone());
        //订单关联的当前用户
        order.setUser(user);

        //从购物车中取出购物项集合
        LinkedHashMap<String, CartItem> cartItemMap = cart.getCartItemMap();
        //创建订单项集合
        List<OrderItem> orderItemList = new ArrayList<>();
        //遍历购物项value值,取出购物项,将对应参数封装到订单项中
        for (CartItem cartItem : cartItemMap.values()) {
            //创建购物项
            OrderItem orderItem = new OrderItem();
            //下单时间
            orderItem.setItemtime(new Date());
            //支付状态与订单一致
            orderItem.setState(order.getState());
            //购买数量
            orderItem.setNum(cartItem.getNum());
            //小计
            orderItem.setSubtotal(cartItem.getItemTotal());
            //订单项所属线路
            orderItem.setRoute(cartItem.getRoute());
            //订单id
            orderItem.setOid(order.getOid());
            //将封装好的订单项存入集合中
            orderItemList.add(orderItem);
        }
        //订单关联订单项列表
        order.setOrderItemList(orderItemList);

        //调用service层保存订单功能
        orderService.save(order);

        //获取jedis连接对象
        Jedis jedis = JedisUtils.getJedis();
        //删除redis中的购物车
        jedis.del("cart_" + user.getUsername());
        //归还连接
        jedis.close();

        //重定向到支付服务
        response.sendRedirect(request.getContextPath()+"/payServlet?action=getPayUrl&oid="+order.getOid());
    }

    //分页查询功能
    public void findByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User user = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (user == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        //已登陆,获取请求参数
        String pageSizeStr = request.getParameter("pageSize");
        String curPageStr = request.getParameter("curPage");
        String stateStr = request.getParameter("state");

        //如果pageSize参数为空,则默认为5
        if (StringUtils.isEmpty(pageSizeStr)) {
            pageSizeStr = "5";
        }

        //如果curPage参数为空,则默认为1(查询第一页数据)
        if (StringUtils.isEmpty(curPageStr)) {
            curPageStr = "1";
        }

        //如果state参数为空,则默认为2
        if (StringUtils.isEmpty(stateStr)) {
            stateStr="2";
        }

        //数据类型转换
        Integer pageSize = Integer.parseInt(pageSizeStr);
        Integer curPage = Integer.parseInt(curPageStr);
        Integer state = Integer.parseInt(stateStr);

        //调用service层分页查询所有功能,返回一个pageBean对象
        PageBean<Order> pageBean=orderService.findByPage(user.getUid(),pageSize,curPage,state);
        //将pageBean和state存入request域中
        request.setAttribute("pb",pageBean);
        request.setAttribute("state",state);
        //转发到订单查看页面展示数据
        request.getRequestDispatcher("home_orderlist.jsp").forward(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }
}
