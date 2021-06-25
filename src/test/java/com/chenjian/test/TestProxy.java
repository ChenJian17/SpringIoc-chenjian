package com.chenjian.test;

import com.chenjian.proxy.JdkProxy;
import com.chenjian.service.OrderService;
import com.chenjian.service.impl.OrderServiceImpl;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by chenjian on 2021/6/19 17:24
 */
public class TestProxy {
//    public static void main(String[] args) {
//        JdkProxy<OrderServiceImpl> jdkProxy = new JdkProxy<>(new OrderServiceImpl());
//        //获取代表类的对象
//        OrderService orderService = (OrderService)jdkProxy.getProxyInstance();
//        orderService.findOrders();
//        System.out.println("131313");
//
//    }

    public static void main(String[] args) {
        //增强类
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(OrderServiceImpl.class);
        //拦截
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("前置通知");
                //调用目标类的方法
                Object result = methodProxy.invokeSuper(o, args);
                System.out.println("后置通知");
                return null;
            }
        });

        //生成了代理类
        OrderServiceImpl orderService = (OrderServiceImpl)enhancer.create();
        orderService.findOrders();
        System.out.println("测试。。。");

    }
}
