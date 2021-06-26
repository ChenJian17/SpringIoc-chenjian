package org.springframework.tx;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by chenjian on 2021/6/26 17:50
 */
public class TransactionManager {

    // ThreadLocal 本地线程局部变量
    // A线程set   A线程get
    // B线程set   B线程get
    // 同一个线程存贮的数据 同一个线程get  数据是同一个
    static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    static {
        Connection connection = getConnection();
        threadLocal.set(connection);
    }

    public static Connection getThreadConnection(){
        return threadLocal.get();
    }

    private static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf-8&serverTimezone=GMT%2B8", "root", "123456");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }
}
