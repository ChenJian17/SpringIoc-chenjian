package com.chenjian.test;

import com.chenjian.controller.OrderController;
import org.springframework.container.ClassPathXmlApplicationContext;

/**
 * Created by chenjian on 2021/6/12 13:11
 */
public class TestSpring {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = ClassPathXmlApplicationContext.getContext();
        context.bootStrap("applicationContext.xml");

//                new ClassPathXmlApplicationContext("applicationContext.xml");
//        OrderController orderController = (OrderController) context.getBean(OrderController.class);
        OrderController orderController = (OrderController) context.getBean("oc");
//        orderController.showInfo();
        orderController.zhuanzhang();
    }
}
