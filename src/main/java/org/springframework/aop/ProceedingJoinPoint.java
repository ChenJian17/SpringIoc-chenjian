package org.springframework.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chenjian on 2021/6/19 19:54
 */
public class ProceedingJoinPoint {
    //目标方法
    private Method method;

    //目标对象
    private Object target;

    //目标方法的参数
    Object[] agrs;

    public ProceedingJoinPoint(Method method, Object target, Object[] agrs) {
        this.method = method;
        this.target = target;
        this.agrs = agrs;
    }

    public Object proceed(){
        try {
            //调用目标方法
            return method.invoke(target,agrs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object[] getAgrs() {
        return agrs;
    }

    public void setAgrs(Object[] agrs) {
        this.agrs = agrs;
    }


}
