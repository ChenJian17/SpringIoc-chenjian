package org.springframework.annotation;

import java.lang.annotation.*;

/**
 * Created by chenjian on 2021/6/11 21:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Autowired {

    String value() default "";
}
