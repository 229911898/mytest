package com.itheima.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet("/BaseServlet")
public class BaseServlet extends HttpServlet {
    //通过反射技术调用方法
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得浏览器请求的action参数
        String action = request.getParameter("action");
        //获得class对象
        Class clazz = this.getClass();
        try {
            //通过clazz对象获得对象中的方法
            Method clazzMethod = clazz.getMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            //调用方法
            clazzMethod.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("非法参数...");
        }
    }

    //将java对象转换为json对象并发送到客户端
    public void javaToJsonWriteClient(HttpServletResponse response, Object object) throws IOException {
        //调用Jackson相关API
        ObjectMapper objectMapper = new ObjectMapper();
        //转换为json对象
        String json = objectMapper.writeValueAsString(object);
        //响应到客户端
        response.getWriter().write(json);
    }
}
