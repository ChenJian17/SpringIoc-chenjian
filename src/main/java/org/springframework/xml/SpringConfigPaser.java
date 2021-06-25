package org.springframework.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenjian on 2021/6/12 11:18
 */
public class SpringConfigPaser {

    /**
     * 解析XML文件，springConfig为传进来的路径
     * @param springConfig
     * @return
     */
    public static String getBasePackage(String springConfig)  {
//        String basePackage = "";
//        try{
//            SAXReader reader = new SAXReader();
//            //通过当前线程获得类加载器的输入流
//            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(springConfig);
//            Document document = reader.read(inputStream);
//            Element rootElement = document.getRootElement();
//            Element element = rootElement.element("context:component-scan");
//            Attribute attribute = element.attribute("base-package");
//            basePackage = attribute.getText();
//
//        }catch (DocumentException e) {
//            e.printStackTrace();
//        }
        //使用try-with-resoure对代码优化
        String basePackage = "";
        SAXReader reader = new SAXReader();
        //通过当前类获取类加载器
        try(InputStream inputStream = SpringConfigPaser.class.getClassLoader().getResourceAsStream(springConfig)) {
            Document document = reader.read(inputStream);
            Element rootElement = document.getRootElement();
            Element element = rootElement.element("component-scan");
            Attribute attribute = element.attribute("base-package");
            basePackage = attribute.getText();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return basePackage;


    }

}
