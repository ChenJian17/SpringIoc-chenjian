package org.springframework.annotation;

import java.lang.annotation.*;

/**
 * Created by chenjian on 2021/6/26 14:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Transactional {
}
