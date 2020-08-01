package com.itheima.travel.web.servlet;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itheima.travel.service.OrderService;
import com.itheima.travel.utils.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/payNotifyServlet")
public class PayNotifyServlet extends HttpServlet {
    OrderService orderService = (OrderService) BeanFactory.getBean("orderService");

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        this.doPost(request,response);
    }

    //支付结果通知
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求输入流
        ServletInputStream is = request.getInputStream();
        //创建xmlMapper对象
        XmlMapper xmlMapper = new XmlMapper();
        //将输入流中的xml格式参数转换为map对象
        Map<String,String> map = xmlMapper.readValue(is, Map.class);

        /*for (String key : map.keySet()) {
            System.out.println(key+": "+map.get(key));
        }*/

        //调用service层检验支付状态,更新订单支付状态
        orderService.payNotify(map);

        //创建响应对象并封装参数
        Map<String, String> msg = new HashMap<>();
        msg.put("return_code", "SUCCESS");
        msg.put("return_msg", "OK");
        //转换为xml格式
        String xml = xmlMapper.writeValueAsString(msg);

        //响应到微信端
        response.setContentType("application/xml");
        response.getWriter().write(xml);
    }
}
