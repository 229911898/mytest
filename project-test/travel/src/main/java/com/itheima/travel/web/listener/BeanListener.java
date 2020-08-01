package com.itheima.travel.web.listener;

import com.itheima.travel.utils.BeanFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;

public class BeanListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //获取ServletContext
        ServletContext servletContext = servletContextEvent.getServletContext();
        //获取初始化参数contextConfigLocation的值
        String contextConfigLocation = servletContext.getInitParameter("contextConfigLocation");
        //通过类加载器获取输入流
        InputStream is = BeanListener.class.getClassLoader().getResourceAsStream(contextConfigLocation);
        //将输入流设置到BeanFactory
        BeanFactory.initBeanFactory(is);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
