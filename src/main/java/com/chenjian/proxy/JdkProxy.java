package com.chenjian.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by chenjian on 2021/6/19 17:18
 */
public class JdkProxy<T> {

    //目标对象
    Object target;

    public JdkProxy(Object target) {
//        this.targetClass = targetClass;
        this.target = target;
    }

    public Object getProxyInstance(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("开始事务");
                //调用目标方法
                Object result = method.invoke(target,args);
                System.out.println("关闭事务");
                return result;
            }
        });
    }


}
