package com.chenjian.service.impl;

import com.chenjian.bean.Order;
import com.chenjian.service.OrderService;
import org.springframework.annotation.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjian on 2021/6/11 21:15
 */
@Service(value = "os1")
public class OrderServiceImpl1 implements OrderService {
    @Override
    public List<Order> findOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(111,"电子产品","2020-10-10",3000));
        orders.add(new Order(222,"图书产品","2020-10-10",3000));
        orders.add(new Order(111,"运动产品","2020-10-10",3000));
        return orders;
    }

    @Override
    public int addOrder(Order o) {
        return 0;
    }
}
