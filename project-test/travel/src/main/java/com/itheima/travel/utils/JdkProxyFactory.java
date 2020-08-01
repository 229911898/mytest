package com.itheima.travel.utils;

import com.itheima.travel.domain.User;
import com.itheima.travel.service.UserService;
import com.itheima.travel.service.impl.UserServiceImpl;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;

/**
 * 日志记录生产代理对象工具类
 */
public class JdkProxyFactory {
    //接收目标对象,要求必须是接口的实现类
    public static Object getLogProxy(Object target) {
        ClassLoader loader = target.getClass().getClassLoader();
        Class[] interfaces = target.getClass().getInterfaces();
        InvocationHandler handler = new InvocationHandler() {
            //编写增强业务逻辑 日志记录
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                Object result = null;

                //开启日志记录
                StringBuffer stringBuffer = new StringBuffer();
                //记录执行时间
                stringBuffer.append("执行时间：" + LocalDateTime.now());
                //记录当前执行的是哪个类
                stringBuffer.append("，执行类：" + target.getClass().getName());
                //记录当前执行的是哪个方法
                stringBuffer.append(",执行方法：" + method.getName());

                //调用目标对象工作
                try {
                    result = method.invoke(target, args);
                } catch (Exception e) {
                    e.printStackTrace();
                    stringBuffer.append(",异常信息：" + e.getCause().getMessage());
                }

                //将日志信息追加写入到日志文件中
                try {
                    File logFile = new File("D:\\logs\\travel.log");
                    if (!logFile.exists()) {
                        logFile.createNewFile();
                    }

                    //创建缓冲流对象
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), "utf-8"));

                    //写入数据
                    bw.write(stringBuffer.toString());
                    bw.newLine();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        };

        //生成代理对象
        Object proxy = Proxy.newProxyInstance(loader, interfaces, handler);
        return proxy;
    }

    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        UserService proxy = (UserService) JdkProxyFactory.getLogProxy(userService);
        User user = proxy.findByUid(1);
        System.out.println(user);
    }
}
