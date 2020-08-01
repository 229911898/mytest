package com.itheima.travel.web.servlet;

import com.itheima.travel.domain.Order;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.service.OrderService;
import com.itheima.travel.utils.BeanFactory;
import com.itheima.travel.utils.PayUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/payServlet")
public class PayServlet extends BaseServlet {
    OrderService orderService = (OrderService) BeanFactory.getBean("orderService");

    //获取微信支付url功能
    public void getPayUrl(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求参数中的订单id
        String oid = request.getParameter("oid");
        //调用orderService根据oid查找订单功能获取order对象
        Order order =orderService.findByOid(oid);
        //获取订单总金额
        Double total = order.getTotal();
        //调用微信支付工具类获取支付url
        //String payUrl = PayUtils.createOrder(oid, total.intValue()*100);
        String payUrl = PayUtils.createOrder(oid, 1);

        //将相关参数存入request域中
        request.setAttribute("order",order);
        request.setAttribute("payUrl",payUrl);

        //转发到支付页面
        request.getRequestDispatcher("pay.jsp").forward(request,response);

    }

    //判断支付状态功能
    public void isPay(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求参数
        String oid = request.getParameter("oid");
        //调用service层判断支付状态功能,获得resultInfo对象
        ResultInfo resultInfo = orderService.isPay(oid);

        //将resultInfo转换成json对象并响应到客户端
        javaToJsonWriteClient(response,resultInfo);
    }

    //支付成功
    public void successPay(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求参数
        String oid = request.getParameter("oid");
        //调用service层根据oid查询获取order对象
        Order order = orderService.findByOid(oid);
        //将order对象存储到request域中
        request.setAttribute("order",order);
        //转发到支付成功页面
        request.getRequestDispatcher("pay_success.jsp").forward(request,response);
    }

    //支付失败
    public void failPay(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求参数
        String oid = request.getParameter("oid");
        //将oid存储到request域中
        request.setAttribute("oid",oid);
        //转发到支付失败页面
        request.getRequestDispatcher("pay_fail.jsp").forward(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }
}
