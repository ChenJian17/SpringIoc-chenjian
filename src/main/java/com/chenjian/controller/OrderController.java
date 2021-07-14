package com.chenjian.controller;

import com.chenjian.bean.Order;
import com.chenjian.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.annotation.Autowired;
import org.springframework.annotation.Controller;
import org.springframework.mvc.annotation.RequestMapping;
import org.springframework.mvc.annotation.RequestParam;
import org.springframework.mvc.annotation.ResponseBody;
import org.springframework.web.bind.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chenjian on 2021/6/11 21:19
 */
@Controller(value = "oc")
@RequestMapping("/order")
public class OrderController {

    @Autowired(value = "os")
    OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/showInfo",method = RequestMethod.GET)
    public List<Order> showInfo(int age,@RequestParam(name = "uname",required = true) String name){
        System.out.println("showInfo方法参数age:"+age);
        System.out.println("showInfo方法参数name:"+name);
        List<Order> orders = orderService.findOrders();
        return orders;
    }

    @RequestMapping("/list")
    public String list(HttpServletRequest request,int age, @RequestParam(name = "uname",required = true) String name, String[] hobbys){
        System.out.println("----->uname="+ name);
        System.out.println("----->age="+ age);
        System.out.println("----->request="+ request);
        System.out.println("----->hobbys="+ Arrays.toString(hobbys));
        request.setAttribute("msg","我是控制器！");
        //默认转发存值
        return "show";  //转发跳转到show.jsp    /WEB-INF/jsp/show.jsp      /WEB-INF/jsp/+视图名+.jsp
    }


    public void add(){
        int count = orderService.addOrder(new Order(1111, "电脑", "2020-10-10", 80000));
        System.out.println(count);
    }

    public void zhuanzhang(){
        int count = orderService.transferMoney("西门庆","金莲",1000);
        System.out.println(count);
    }
}
