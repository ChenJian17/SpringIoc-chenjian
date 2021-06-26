package org.springframework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.tx.TransactionManager;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;

/**
 * Created by chenjian on 2021/6/26 15:05
 */
public class CglibProxy {

    /**
     * 要代理的类
     */
    Class<?> targetClass;

    /**
     * 哪些方法要被代理
     */
    List<String> transactionalMethods;

    public CglibProxy(Class<?> targetClass, List<String> transactionalMethods) {
        this.targetClass = targetClass;
        this.transactionalMethods = transactionalMethods;
    }

    public Object getProxyInstance(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                Object result = null;
                if (transactionalMethods.contains(method.getName())){
                    System.out.println("关闭事务自动提交......");
                    Connection connection = TransactionManager.getThreadConnection();
                    connection.setAutoCommit(false);
                    try {
                        //调用目标方法
                        result = methodProxy.invokeSuper(o,args);
                        System.out.println("手动提交事务..........");
                        connection.commit();

                    } catch (Throwable throwable){
                        System.out.println("回滚事务..........");
                        connection.rollback();
                    } finally {

                    }


                }else {
                    result = methodProxy.invokeSuper(o,args);
                }
                return result;
            }
        });
        return enhancer.create();
    }
}
