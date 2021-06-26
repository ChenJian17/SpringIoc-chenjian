package com.chenjian.service;

import com.chenjian.bean.Order;

import java.util.List;

/**
 * Created by chenjian on 2021/6/11 21:14
 */
public interface OrderService {

    List<Order> findOrders();

    int addOrder(Order o);

    int transferMoney(String fromName,String toName,Integer money);
}
