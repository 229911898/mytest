package com.itheima.travel.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

public class BeanFactory {
    private static Document document;

    public static void initBeanFactory(InputStream inputStream) {
        try {
            //创建dom4j解析器对象
            SAXReader saxReader = new SAXReader();
            //获取document对象
            document = saxReader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("配置文件加载失败");
        }
    }

    public static Object getBean(String id) {
        Object object = null;
        //获取类加载器
        try {
            //根据id拼接Xpath表达式
            String xpath="//bean[@id='"+id+"']";
            //获取指定标签
            Element element = (Element) document.selectSingleNode(xpath);
            //获取class属性值,返回全限定名
            String className = element.attributeValue("class");
            //获取该类字节码对象
            Class clazz = Class.forName(className);
            //创建该类的对象
            object=clazz.newInstance();
            //如果是业务层,增加日志记录,创建代理对象
            if (id.endsWith("Service")) {
                object = JdkProxyFactory.getLogProxy(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("没有指定的对象");
        }
        //返回对象
        return object;
    }
}
