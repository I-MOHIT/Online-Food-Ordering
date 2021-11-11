package com.company.services;

import com.company.entities.Order;

import java.util.List;

public class OrderService {
    private static OrderService orderService = null;

    private OrderService(){

    }

    public static OrderService getInstance(){
        if(orderService==null){
            orderService = new OrderService();
        }
        return orderService;
    }

    UserDOA userDOA = UserDOA.getInstance();

    public Order placeOrder(String name, Integer quantity){
        if(quantity<=0){
            System.out.println("Quantity can't be less than 0");
            return null;
        }
        return userDOA.placeOrder(name, quantity);
    }

    public List<Order> listOrders(){
        return userDOA.orderList();
    }
}
