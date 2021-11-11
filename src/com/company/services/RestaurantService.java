package com.company.services;

import com.company.entities.Restaurant;
import com.company.entities.Review;

import java.util.List;

public class RestaurantService {
    private static RestaurantService restaurantService = null;
    private RestaurantService(){

    }

    public static RestaurantService getInstance(){
        if(restaurantService==null){
            restaurantService = new RestaurantService();
        }
        return restaurantService;
    }

    UserDOA userDOA = UserDOA.getInstance();

    public Restaurant registerRestaurant(String name, String pincodes, String item, int price, int quantity){
        if(price<=0 || quantity<=0){
            System.out.println("Invalid values for price and/or quantity");
            return null;
        }else if(name.isEmpty()){
            System.out.println("Name cannot be empty");
            return null;
        }
        return userDOA.registerRestaurant(name, pincodes, item, price, quantity);
    }

    public Review rateRestaurant(String name, Integer rating, String comment){
        if(rating==null || rating <=0 || rating>5){
            System.out.println("Invalid rating");
            return null;
        }
        return userDOA.reviewRestaurant(name, rating, comment);
    }

    public Restaurant updateQuantity(String name, int quantity){
        if(quantity<=0){
            System.out.println("Quantity cannot be less than zero");
            return null;
        }
        return userDOA.updateQuantity(name, quantity);
    }

    public List<Restaurant> showRestaurants(String restaurants){
        return userDOA.showRestaurant(restaurants);
    }
}
