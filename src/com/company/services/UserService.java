package com.company.services;

import com.company.Gender;
import com.company.entities.User;

public class UserService {
    private static UserService userService = null;
    private UserService(){

    }

    public static UserService getInstance(){
        if(userService==null){
            userService = new UserService();
        }
        return userService;
    }

    UserDOA userDOA = UserDOA.getInstance();

    public User registerUser(Long phone, String name, Long pincode, Gender gender){
        if(phone== null || phone<=0) {
            System.out.println("Invalid phone number");
            return null;
        }else if(pincode==null||pincode<=0){
            System.out.println("Invalid pincode");
            return null;
        }else if(name.isEmpty()){
            System.out.println("Invalid name");
            return null;
        }
        return userDOA.registerUser(phone, name, pincode, gender);
    }

    public User login(Long phone){
        return userDOA.loginUser(phone);
    }
}
