package com.company;

import com.company.entities.Order;
import com.company.entities.Restaurant;
import com.company.entities.Review;
import com.company.entities.User;
import com.company.services.OrderService;
import com.company.services.RestaurantService;
import com.company.services.UserService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Starting application \n");
        UserService userService = UserService.getInstance();
        RestaurantService restaurantService = RestaurantService.getInstance();
        OrderService orderService = OrderService.getInstance();

        User user1 = userService.registerUser(123456L, "ABCD", 1234L, Gender.M);
        User user2 = userService.registerUser(135791L, "EFGH", 5678L, Gender.F);
        User user3 = userService.registerUser(246802L, "IJKL", 1357L, Gender.M);

        User loggedInUser = userService.login(user1.getPhone());

        Restaurant r1 = restaurantService.registerRestaurant("ra", "1234,5678", "Pizza", 100, 10);
        Restaurant r2 = restaurantService.registerRestaurant("rb", "5678,1357", "Burger", 50, 50);

        r1 = restaurantService.updateQuantity("ra", 2);

        loggedInUser = userService.login(user2.getPhone());

        restaurantService.showRestaurants("rating");

        Order order = orderService.placeOrder("ra", 4);

        Review review = restaurantService.rateRestaurant("rb", 5, "Great");

        List<Order> orderList = orderService.listOrders();

        System.out.println("All test cases tried");
    }
}
