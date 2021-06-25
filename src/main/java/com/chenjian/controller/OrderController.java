package com.chenjian.controller;

import com.chenjian.bean.Order;
import com.chenjian.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.annotation.Autowired;
import org.springframework.annotation.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjian on 2021/6/11 21:19
 */
@Controller(value = "oc")
public class OrderController {

    @Autowired(value = "os")
    OrderService orderService;

    public void showInfo(){
        List<Order> orders = orderService.findOrders();
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public void add(){
        int count = orderService.addOrder(new Order(1111, "电脑", "2020-10-10", 80000));
        System.out.println(count);
    }
}
