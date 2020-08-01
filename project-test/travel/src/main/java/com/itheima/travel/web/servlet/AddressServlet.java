package com.itheima.travel.web.servlet;

import com.itheima.travel.domain.Address;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.User;
import com.itheima.travel.service.AddressService;
import com.itheima.travel.utils.BeanFactory;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/addressServlet")
public class AddressServlet extends BaseServlet {
    AddressService addressService = (AddressService) BeanFactory.getBean("addressService");

    //根据uid查询所有功能
    public void findAllByUid(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (currentUser == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        //已登陆,调用service层根据uid查询所有功能,返回list集合
        List<Address> list = addressService.findAllByUid(currentUser.getUid());
        //将list对象存入request域中
        request.setAttribute("list", list);
        //转发到个人中心地址管理页面回显数据
        request.getRequestDispatcher("home_address.jsp").forward(request, response);
    }

    //更新功能
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (currentUser == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try {
            //已登陆,获取请求参数的map集合
            Map<String, String[]> parameterMap = request.getParameterMap();
            //封装参数到address对象中
            Address address = new Address();
            BeanUtils.populate(address, parameterMap);

            //调用service层更新功能
            addressService.update(address);
            //调用查询所有功能回显数据
            findAllByUid(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("服务器开小差了...");
        }
    }

    //保存功能
    public void save(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (currentUser == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try {
            //已登陆,获取请求参数的map集合
            Map<String, String[]> parameterMap = request.getParameterMap();
            //封装参数到address对象中
            Address address = new Address();
            BeanUtils.populate(address, parameterMap);

            //将当前登陆用户封装到address中
            address.setUser(currentUser);
            //调用service层存储数据到数据库
            addressService.save(address);
            //调用查询所有功能回显数据
            findAllByUid(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("服务器开小差了...");
        }
    }

    //根据aid查询功能
    public void findByAid(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (currentUser == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        //已登陆,获取请求参数aid
        String aidStr = request.getParameter("aid");
        //转换为int类型
        int aid = Integer.parseInt(aidStr);
        //调用service层根据aid查询功能,获得address对象
        Address address = addressService.findByAid(aid);
        //将address对象转换为json对象并响应到客户端
        javaToJsonWriteClient(response, address);
    }

    //删除功能
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (currentUser == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        //已登陆,获取请求参数aid
        String aidStr = request.getParameter("aid");
        //转换为int类型
        int aid = Integer.parseInt(aidStr);
        //调用service层删除功能
        addressService.delete(aid);
        //调用查询所有功能回显数据
        findAllByUid(request, response);
    }

    //设为默认功能
    public void isDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (currentUser == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        //已登陆,调用service层根据uid查询所有功能,返回list集合
        List<Address> list = addressService.findAllByUid(currentUser.getUid());
        //遍历集合,保证当前user只有一个默认地址
        for (Address address : list) {
            //判断当前user对象是否已经设置过默认地址
            if ("1".equals(address.getIsdefault())) {
                //将默认地址修改为非默认
                address.setIsdefault("0");
                //调用service层更新功能,将修改后的address更新到数据库
                addressService.update(address);
            }
        }

        //获取请求参数aid
        String aidStr = request.getParameter("aid");
        //转换为int类型
        int aid = Integer.parseInt(aidStr);
        //调用service层根据aid查询功能,获得address对象
        Address address = addressService.findByAid(aid);
        //设置为默认地址
        address.setIsdefault("1");
        //调用service层更新数据到数据库
        ResultInfo resultInfo = addressService.update(address);
        //将resultInfo对象转换为json对象并响应到客户端
        javaToJsonWriteClient(response, resultInfo);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }
}
