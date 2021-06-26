package com.chenjian.service.impl;

import com.chenjian.bean.Order;
import com.chenjian.dao.OrderDao;
import com.chenjian.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.annotation.Autowired;
import org.springframework.annotation.Service;
import org.springframework.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjian on 2021/6/11 21:15
 */
@Service(value = "os")
public class OrderServiceImpl  implements OrderService{
    @Autowired
    OrderDao orderDao;

    public List<Order> findOrders() {
//        System.out.println("业务逻辑代码...................");\
        System.out.println("add 产品");
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(111,"电子产品","2020-10-10",3000));
        orders.add(new Order(222,"图书产品","2020-10-10",3000));
        orders.add(new Order(111,"运动产品","2020-10-10",3000));
        return orders;
//        return orderDao.findOrders();
    }

    public int addOrder(Order o) {
        return 1;
    }

    @Transactional
    public int transferMoney(String fromName, String toName, Integer money) {
        int count1 = orderDao.excuteMoney(fromName,money*(-1));
        System.out.println(count1>0?fromName+"转账成功":fromName+"转账失败");
        System.out.println("-------------------------------------------");

//        int i=100/0;


        int count2 = orderDao.excuteMoney(toName,money);
        System.out.println(count2>0?toName+"收款成功":toName+"收款失败");

        return count1+count2;
    }


}
