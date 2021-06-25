package com.chenjian.dao.impl;

import com.chenjian.bean.Order;
import com.chenjian.dao.OrderDao;
import org.springframework.annotation.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: SpringIoc-bruceliu
 * @BelongsPackage: com.bruceliu.dao.impl
 * @CreateTime: 2020-10-10 15:35
 * @Description: TODO
 */
@Repository
public class OrderDaoImpl implements OrderDao {

    public List<Order> findOrders() {
        System.out.println("Dao代码......");
        List<Order> orders=new ArrayList<Order>();
        orders.add(new Order(111,"电子产品","2020-10-10",3000));
        orders.add(new Order(222,"图书产品","2020-10-10",3000));
        orders.add(new Order(111,"运动产品","2020-10-10",8888));
        return orders;
    }
}
