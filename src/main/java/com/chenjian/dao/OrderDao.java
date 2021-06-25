package com.chenjian.dao;

import com.chenjian.bean.Order;

import java.util.List;

/**
 * @BelongsProject: SpringIoc-bruceliu
 * @BelongsPackage: com.bruceliu.dao
 * @CreateTime: 2020-10-10 15:34
 * @Description: TODO
 */
public interface OrderDao {

    List<Order> findOrders();
}
