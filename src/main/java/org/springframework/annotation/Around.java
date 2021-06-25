package org.springframework.annotation;

import java.lang.annotation.*;

/**
 * Created by chenjian on 2021/6/19 19:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Around {

    String execution() default "";
}
