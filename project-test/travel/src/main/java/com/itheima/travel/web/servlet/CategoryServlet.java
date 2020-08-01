package com.itheima.travel.web.servlet;

import com.itheima.travel.domain.Category;
import com.itheima.travel.service.CategoryService;
import com.itheima.travel.utils.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/categoryServlet")
public class CategoryServlet extends BaseServlet {
    CategoryService categoryService= (CategoryService) BeanFactory.getBean("categoryService");

    //查询所有功能
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //调用service层查询所有功能,获得list集合
       List<Category> list=categoryService.findAll();
       //将list对象转换为json并响应到客户端
       javaToJsonWriteClient(response,list);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }
}
