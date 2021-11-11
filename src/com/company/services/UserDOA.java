package com.company.services;

import com.company.Gender;
import com.company.entities.Order;
import com.company.entities.Restaurant;
import com.company.entities.Review;
import com.company.entities.User;

import java.util.*;


public class UserDOA {
    private static UserDOA userDOA = null;

    private UserDOA(){

    }

    //Singleton to ensure only one user at a time
    public static UserDOA getInstance(){
        if(userDOA == null){
            userDOA = new UserDOA();
        }
        return userDOA;
    }

    private HashMap<Integer, User> userHashMap = new HashMap<>();
    private HashMap<Long, Integer> phoneHashMap = new HashMap<>();
    private HashMap<String, Restaurant> restaurantHashMap = new HashMap<>();

    private User loggedInUser = null; //No user logged in the beginning

    public User registerUser(Long phone, String name, Long pincode, Gender gender){
        if(phoneHashMap.containsKey(phone)){
            User user = userHashMap.get(phoneHashMap.get(phone));
            System.out.println("User already registered with this phone no");
            return user;
        }

        User user = new User(IDGenerator.getId(), name, gender, phone, pincode);
        phoneHashMap.put(phone,user.getId());
        userHashMap.put(user.getId(),user);
        System.out.println("Created new user successfully");
        return user;
    }

    public User loginUser(Long phone){
        if(!phoneHashMap.containsKey(phone)) {
            System.out.println("This phone number has no user account");
            return null;
        }
        User user = userHashMap.get(phoneHashMap.get(phone));

        loggedInUser = user;
        System.out.println("Logged in successfully");

        return user;
    }

    public Restaurant registerRestaurant(String name, String pincodes, String item, int price, int quantity){
        if(loggedInUser == null){
            System.out.println("No logged in user");
            return null;
        }
        if(restaurantHashMap.containsKey(name)){
            System.out.println("Restaurant already exists");
            return null;
        }

        List<String> pincodeList = Arrays.asList(pincodes.split(","));
        List<Long> pins = new ArrayList<>();
        if(!pincodes.isEmpty()){
            for(String s : pincodeList){
                if(!s.chars().allMatch(Character::isDigit)){
                    System.out.println("Invalid pincode");
                    return null;
                }
                pins.add(Long.parseLong(s));
            }
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setId(IDGenerator.getId());
        restaurant.setName(name);
        restaurant.setItem(item);
        restaurant.setQuantity(quantity);
        restaurant.setPrice(price);
        restaurant.setServiceableAreaPincodes(pins);
        restaurant.setCreatedBy(loggedInUser.getId());
        restaurantHashMap.put(name,restaurant);
        loggedInUser.getRestaurants().add(restaurant);
        System.out.println("New restaurant created!");

        return restaurant;
    }

    public Review reviewRestaurant(String name, Integer rating, String comment){
        Restaurant restaurant = restaurantHashMap.get(name);

        if(restaurant == null){
            System.out.println("No restaurant found with this name");
            return null;
        }
        if(loggedInUser.getId()!=restaurant.getCreatedBy()){
            System.out.println("Logged in user is not the owner of this restaurant");
            return null;
        }

        Review review = new Review();

        review.setId(IDGenerator.getId());
        review.setComment(comment);
        review.setScore(rating);

        if(restaurant.getReviews()==null || restaurant.getReviews().size() == 0){
            restaurant.setRating(Float.valueOf(rating));
        }else{
            float currentScore = (restaurant.getRating()*restaurant.getReviews().size()+rating)/(restaurant.getReviews().size()+1);
            restaurant.setRating(currentScore);
        }
        restaurant.getReviews().add(review);
        return review;
    }

    public Restaurant updateQuantity(String name, int quantity){
        Restaurant restaurant = restaurantHashMap.get(name);
        if(restaurant == null){
            System.out.println("No such restaurant");
            return null;
        }
        if(loggedInUser.getId()!=restaurant.getCreatedBy()){
            System.out.println("Logged in user not the owner of this restaurant");
            return null;
        }
        restaurant.setQuantity(restaurant.getQuantity()+quantity);
        return restaurant;
    }

    public List<Restaurant> showRestaurant(String restaurants){
        List<Restaurant> r = loggedInUser.getRestaurants();
        List<Restaurant> restaurantsList = new ArrayList<>();

        for(Restaurant restaurant:r){
            if(restaurant.getServiceableAreaPincodes().contains(loggedInUser.getPincode()) && restaurant.getQuantity()>0){
                restaurantsList.add(restaurant);
            }
        }

        if(restaurants.equalsIgnoreCase("rating")){
            Collections.sort(restaurantsList,new SortByRating());
            for(Restaurant restaurant: restaurantsList){
                System.out.println("Restaurant Name: " + restaurant.getName() + " Restaurant Id: " + restaurant.getId() + " Restaurant Price: " + restaurant.getPrice() + " Restaurant Rating: " + restaurant.getRating());
            }
            return restaurantsList;
        }

        Collections.sort(restaurantsList, new SortByPrice());
        for(Restaurant restaurant: restaurantsList){
            System.out.println("Restaurant Name: " + restaurant.getName() + " Restaurant Id: " + restaurant.getId() + " Restaurant Price: " + restaurant.getPrice() + " Restaurant Rating: " + restaurant.getRating());
        }
        return restaurantsList;
    }

    public Order placeOrder(String name, Integer quantity){
        Restaurant restaurant = restaurantHashMap.get(name);

        if(restaurant == null){
            System.out.println("No restaurant with this name");
            return null;
        }
        if(loggedInUser.getId()!=restaurant.getCreatedBy()){
            System.out.println("Logged in user is not the owner of this restaurant");
            return null;
        }
        if(restaurant.getQuantity()==0){
            System.out.println("Restaurant out of stock");
            return null;
        }
        if(restaurant.getQuantity()<quantity){
            System.out.println("Restaurant has less quantity than required for order");
            return null;
        }

        Order order = new Order();
        order.setId(IDGenerator.getId());
        order.setItem(restaurant.getItem());
        order.setOrdertime(System.currentTimeMillis());
        order.setQuantity(quantity);
        order.setUserId(loggedInUser.getId());
        order.setCost((long)(quantity*restaurant.getPrice()));
        order.setRestaurantId(restaurant.getId());

        restaurant.setQuantity(restaurant.getQuantity()-quantity);
        loggedInUser.getOrders().add(order);

        return order;
    }

    public List<Order> orderList(){
        for (Order order: loggedInUser.getOrders()){
            System.out.println("Order ID: " + order.getId() + " Order Item: " + order.getItem() + " Order Time: " + order.getOrdertime() + " Order Quantity: " + order.getQuantity() + " Order Cost: " + order.getCost() + " Order Restaurant: " + order.getRestaurantId());
        }

        return loggedInUser.getOrders();
    }

    class SortByRating implements Comparator<Restaurant>{
        @Override
        public int compare(Restaurant r1, Restaurant r2){
            if(r1.getRating()==null || r2.getRating()==null){
                return 0;
            }
            if(r1.getRating() > r2.getRating()){
                return 1;
            }else if(r1.getRating()==r2.getRating()){
                return 0;
            }else{
                return -1;
            }
        }
    }

    class SortByPrice implements Comparator<Restaurant>{
        @Override
        public int compare(Restaurant r1, Restaurant r2){
            return r1.getPrice() - r2.getPrice();
        }
    }
}
