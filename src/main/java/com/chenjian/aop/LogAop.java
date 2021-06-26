package com.chenjian.aop;

import org.springframework.annotation.Around;
import org.springframework.annotation.Aspect;
import org.springframework.aop.ProceedingJoinPoint;

/**
 * Created by chenjian on 2021/6/19 20:02
 */
@Aspect
public class LogAop {

//    @Around(execution = "com.chenjian.service.impl.OrderServiceImpl.addOrder")
    public Object around(ProceedingJoinPoint joinPoint){
        Object result=null;
        try {
            System.out.println("==>前置日志通知.......");
            //调用目标对象的目标方法
            result=joinPoint.proceed();
            System.out.println("==>返回日志通知.......");
        } catch (Throwable throwable) {
            System.out.println("==>异常日志通知......."+throwable.getMessage());
        } finally {
            System.out.println("==>最终日志通知.......");
        }
        return result;
    }

}