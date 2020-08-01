package com.itheima.travel.web.servlet;

import com.itheima.travel.domain.Cart;
import com.itheima.travel.domain.CartItem;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.User;
import com.itheima.travel.service.RouteService;
import com.itheima.travel.utils.BeanFactory;
import com.itheima.travel.utils.CartUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;

@WebServlet("/cartServlet")
public class CartServlet extends BaseServlet {
    RouteService routeService = (RouteService) BeanFactory.getBean("routeService");

    //添加线路到购物车功能
    public void addCart(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求参数
        String numStr = request.getParameter("num");
        String rid = request.getParameter("rid");
        //转换数据类型
        int num = Integer.parseInt(numStr);
        //调用routeService根据rid查询线路功能,获得route对象
        Route route = routeService.findByDetail(rid);

        //从session域中取出当前登陆user对象
        User user = (User) request.getSession().getAttribute("currentUser");
        //从redis中获取该user的购物车
        Cart cart = CartUtils.getCartFromRedis(user);
        //从购物车中获取购物项集合
        LinkedHashMap<String, CartItem> cartItemMap = cart.getCartItemMap();
        //根据rid获取对应购物项
        CartItem cartItem = cartItemMap.get(rid);
        //判断购物项是否存在
        if (cartItem != null) {
            //存在,则追加购物项数量
            cartItem.setNum(cartItem.getNum() + num);
        } else {
            //不存在,则创建该购物项并存入数据
            cartItem = new CartItem();
            cartItem.setRoute(route);
            cartItem.setNum(num);
        }

        //更新集合数据
        cartItemMap.put(rid, cartItem);

        //将修改后的购物车存入redis缓存中
        CartUtils.setCartToRedis(user,cart);
        //将购物项存入request域中
        request.setAttribute("cartItem",cartItem);
        //转发到购物项成功添加页面
        request.getRequestDispatcher("cart_success.jsp").forward(request,response);
    }

    //查询购物车功能
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User user = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (user == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        //已登陆,从redis中获取该用户购物车,获得cart对象
        Cart cart = CartUtils.getCartFromRedis(user);
        //将cart对象存入request域中
        request.setAttribute("cart",cart);
        //转发到购物车页面
        request.getRequestDispatcher("cart.jsp").forward(request,response);
    }

    //删除功能
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
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
        String rid = request.getParameter("rid");
        //从redis中获取该用户购物车,获得cart对象
        Cart cart = CartUtils.getCartFromRedis(user);
        //从购物车中获取购物项集合
        LinkedHashMap<String, CartItem> cartItemMap = cart.getCartItemMap();
        //删除rid所对应的购物项
        cartItemMap.remove(rid);

        //将cart对象更新到redis缓存
        CartUtils.setCartToRedis(user,cart);
        //将cart对象存入request域中
        request.setAttribute("cart",cart);
        //转发到购物车页面
        request.getRequestDispatcher("cart.jsp").forward(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    }
}
