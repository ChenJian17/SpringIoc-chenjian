package org.springframework.annotation;

import java.lang.annotation.*;

/**
 * Created by chenjian on 2021/6/11 21:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Controller {

    String value() default "";
}