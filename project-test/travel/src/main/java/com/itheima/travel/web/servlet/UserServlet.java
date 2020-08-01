package com.itheima.travel.web.servlet;

import cn.hutool.core.util.IdUtil;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.User;
import com.itheima.travel.service.UserService;
import com.itheima.travel.utils.BeanFactory;
import com.itheima.travel.utils.JedisUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Map;

@WebServlet("/userServlet")
@MultipartConfig
public class UserServlet extends BaseServlet {
    UserService userService = (UserService) BeanFactory.getBean("userService");

    //注册功能
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            //先进行短信验证码校验
            //从请求参数中获取手机号码
            String telephone = request.getParameter("telephone");
            //从连接池获取jedis对象
            Jedis jedis = JedisUtils.getJedis();
            //从redis中获取smsCode
            String smsCodeServer = jedis.get("smsCode" + telephone);
            //获取请求参数中的smsCode
            String smsCodeClient = request.getParameter("smsCode");
            //判断短信验证码是否匹配
            if (!smsCodeClient.equals(smsCodeServer)) {
                //不匹配说明验证码错误,将相关提示信息封装到resultInfo中
                ResultInfo resultInfo = new ResultInfo(false, "验证码不正确");
                //将resultInfo存放到request域中
                request.setAttribute("resultInfo", resultInfo);
                //转发到注册页面并返回
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            //获取请求参数的map集合
            Map<String, String[]> parameterMap = request.getParameterMap();
            //封装到user对象中
            User user = new User();
            BeanUtils.populate(user, parameterMap);
            //调用service层方法获得一个resultInfo对象
            ResultInfo resultInfo = userService.register(user);
            //判断注册是否成功
            if (resultInfo.getSuccess()) {
                //成功则重定向到register_ok页面
                response.sendRedirect(request.getContextPath() + "/register_ok.jsp");
                //删除redis中的smsCode
                jedis.del("smsCode" + telephone);
            } else {
                //不成功则将resultInfo存到request域中
                request.setAttribute("resultInfo", resultInfo);
                //转发到注册页面
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
            //归还连接
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("服务器开小差了...");
        }
    }

    //根据用户名查找功能
    public void findByUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求参数
        String username = request.getParameter("username");
        //调用service层方法获得一个resultInfo对象
        ResultInfo resultInfo = userService.findByUsername(username);
        //将resultInfo对象转换为json对象并响应到客户端
        javaToJsonWriteClient(response, resultInfo);
    }

    //根据手机号码查找功能
    public void findByTelephone(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求参数
        String telephone = request.getParameter("telephone");
        //调用service层方法获得一个resultInfo对象
        ResultInfo resultInfo = userService.findByTelephone(telephone);
        //将resultInfo对象转换为json对象并响应到客户端
        javaToJsonWriteClient(response, resultInfo);
    }

    //发送短信验证码功能
    public void sendSmsCode(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获取请求参数
        String telephone = request.getParameter("telephone");
        //获取一个随机6位数
        String smsCode = RandomStringUtils.randomNumeric(6);

        //调用service层方法获得一个resultInfo对象
        ResultInfo resultInfo = userService.sendSmsCode(telephone, smsCode);
        //从连接池获取jedis对象
        Jedis jedis = JedisUtils.getJedis();
        //将短信验证码存入redis中,并设置存活时间五分钟
        jedis.setex("smsCode" + telephone, 300, smsCode);
        //归还连接
        jedis.close();
        //输出短信验证码到控制台
        System.out.println("注册验证码:" + smsCode);
        //将resultInfo对象转换为json对象并响应到客户端
        javaToJsonWriteClient(response, resultInfo);
    }

    //用户名密码登陆功能
    public void pwdLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            //获取请求参数的map集合
            Map<String, String[]> parameterMap = request.getParameterMap();
            //封装到user对象中
            User user = new User();
            BeanUtils.populate(user, parameterMap);
            //调用service层方法获得一个resultInfo对象
            ResultInfo resultInfo = userService.pwdLogin(user);

            //判断是否登陆成功
            if (resultInfo.getSuccess()) {
                //登陆成功,获取user对象并存入session域中
                request.getSession().setAttribute("currentUser", resultInfo.getData());
            }

            //将resultInfo对象转换为json对象并响应到客户端
            javaToJsonWriteClient(response, resultInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("服务器开小差了...");
        }
    }

    //手机号和短信验证码登陆功能
    public void telLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //获得请求参数中的手机号码
        String telephone = request.getParameter("telephone");
        //调用service层方法根据手机号码查询获得一个resultInfo对象
        ResultInfo resultInfo = userService.findByTelephone(telephone);
        //判断手机号码是否存在
        if (resultInfo.getSuccess()) {
            //手机号码存在,获取请求参数中的smsCode
            String smsCodeClient = request.getParameter("smsCode");
            //从连接池获取jedis对象
            Jedis jedis = JedisUtils.getJedis();
            //从redis中获取smsCode
            String smsCodeServer = jedis.get("smsCode" + telephone);
            //判断短信验证码是否匹配
            if (!smsCodeClient.equals(smsCodeServer)) {
                //不匹配说明验证码错误,将相关提示信息封装到resultInfo对象中
                resultInfo = new ResultInfo(false, "验证码不正确");
            } else {
                //手机号和验证码都正确,清除redis中的smsCode
                jedis.del("smsCode" + telephone);
                //获取resultInfo中封装的user对象并存到session域中
                request.getSession().setAttribute("currentUser", resultInfo.getData());
                //登陆成功,将相关提示信息封装到resultInfo对象中
                resultInfo = new ResultInfo(true, "登陆成功");
            }
            //归还连接
            jedis.close();
        } else {
            //手机号码不存在,将相关提示信息封装到resultInfo对象中
            resultInfo = new ResultInfo(false, "此手机号尚未注册");
        }

        //将resultInfo对象转换为json对象并响应到客户端
        javaToJsonWriteClient(response, resultInfo);
    }

    //退出登陆功能
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //销毁session
        request.getSession().removeAttribute("currentUser");
        //重定向到首页
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    //进入个人中心功能
    public void userInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //判断当前用户是否登陆状态,从session域中获取currentUser对象
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        //判断currentUser对象是否为空
        if (currentUser == null) {
            //为空,说明登录失效,重定向到首页并返回
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        //已登陆,则重定向到个人信息页面
        response.sendRedirect(request.getContextPath() + "/home_index.jsp");
    }

    //更新个人信息功能
    public void updateInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            //判断当前用户是否登陆状态,从session域中获取currentUser对象
            User currentUser = (User) request.getSession().getAttribute("currentUser");
            //判断currentUser对象是否为空
            if (currentUser == null) {
                //为空,说明登录失效,重定向到首页并返回
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            //已登陆,获取请求参数的map集合
            Map<String, String[]> parameterMap = request.getParameterMap();
            //封装到user对象中
            User user = new User();
            BeanUtils.populate(user, parameterMap);

            //获取文件对象
            Part part = request.getPart("pic");
            //获取文件名
            String fileName = part.getSubmittedFileName();

            //判断是否有上传文件
            if (fileName.length() > 0) {
                //给文件重命名:图片文件项目路径+随机字符串+文件名
                String picPath = "/pic/" + IdUtil.simpleUUID() + fileName;
                //获取文件的磁盘真实路径
                String realPath = request.getServletContext().getRealPath(picPath);
                System.out.println(realPath);

                //保存文件
                part.write(realPath);
                //将文件名存入user中
                user.setPic(picPath);
            }

            //调用service层更新个人信息
            userService.updateInfo(user);
            //调用service层根据uid查询获取user对象
            currentUser = userService.findByUid(user.getUid());
            //将user对象更新到session域中
            request.getSession().setAttribute("currentUser", currentUser);
            //重定向到个人中心页面回显数据
            response.sendRedirect(request.getContextPath() + "/home_index.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("服务器开小差了...");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    }
}
