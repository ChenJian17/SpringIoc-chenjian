package com.chenjian.dao.impl;

import com.chenjian.bean.Order;
import com.chenjian.dao.OrderDao;
import org.springframework.annotation.Repository;
import org.springframework.tx.TransactionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public int addOrder(Order o) {
        System.out.println("Dao层的新增Order....");
        return 1;
    }

    public int excuteMoney(String name, Integer money) {
//        Connection connection = null;
        int result = 0;
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e){
//            e.printStackTrace();
//        }

        try
        {
            Connection connection = TransactionManager.getThreadConnection();
            PreparedStatement ps = connection.prepareStatement("update t_account set money=money+? where name=?");
            ps.setObject(1,money);
            ps.setObject(2,name);
            result = ps.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            //没有关闭
        }



//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf-8&serverTimezone=GMT%2B8","root", "123456");
//            PreparedStatement ps = connection.prepareStatement("update t_account set money=money+? where name=?");
//            ps.setObject(1,money);
//            ps.setObject(2,name);
//            result = ps.executeUpdate();
//
//        } catch (Exception e){
//            e.printStackTrace();
//        } finally {
//            if (connection != null){
//                try {
//                    connection.close();
//                } catch (SQLException e){
//                    e.printStackTrace();
//                }
//            }
//
//        }

        return result;

    }
}
