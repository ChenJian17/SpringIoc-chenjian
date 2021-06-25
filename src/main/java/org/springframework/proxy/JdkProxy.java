package org.springframework.proxy;

import org.springframework.aop.ProceedingJoinPoint;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by chenjian on 2021/6/20 15:50
 */
public class JdkProxy<T> {

    /**
     * 目标类
     */
//    Class<?> targetClass;

    /**
     * 切面类对象
     */
    Class<?> aopClass;

    /**
     * 目标对象
     */
    Object targetObject;

    /**
     * 要被代理的方法的名字
     */
    String methodName;

    /**
     * AOP切面类的方法
     */
    Method aopMethod;


    /**
     * 获取JDK代理对象
     * @return
     */
    public Object getProxyInstance(){
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //调用目标对象目标方法
                if (methodName.equals(method.getName())){
                    ProceedingJoinPoint joinPoint = new ProceedingJoinPoint(method,targetObject,args);
                    return aopMethod.invoke(aopClass.newInstance(), joinPoint);
                }else {
                    return method.invoke(targetObject,args);
                }
            }
        });

    }

    public JdkProxy(Class<?> aopClass, Object targetObject, String methodName, Method aopMethod) {
        this.aopClass = aopClass;
        this.targetObject = targetObject;
        this.methodName = methodName;
        this.aopMethod = aopMethod;
    }
}
