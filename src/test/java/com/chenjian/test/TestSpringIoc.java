package com.chenjian.test;

import org.springframework.container.ClassPathXmlApplicationContext;

/**
 * Created by chenjian on 2021/6/12 11:11
 */
public class TestSpringIoc {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext =
                new ClassPathXmlApplicationContext("applicationContext.xml");

    }
}
