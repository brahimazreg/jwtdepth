package com.brahim.controller;


import com.brahim.model.Role;
import com.brahim.model.User;
import com.brahim.projection.Test;
import com.brahim.projection.UserProjection;
import com.brahim.service.MyUserService;
import org.hibernate.annotations.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private MyUserService myUserService;

    @PostMapping("/addUser")
    public User createUser(@RequestBody User user){
        return myUserService.createUser(user);
    }
    @PostMapping("/addRole")
    public Role createRole(@RequestBody Role role){
        return myUserService.createRole(role);
    }

    @PostMapping("/addRoleToUser")
    public String addRoleToUser(@RequestBody String username,String roleName){
        myUserService.addRoleToUser(username,roleName);
        return " a role has been added to user";
    }

    @GetMapping("/user")
    public User getUser(@RequestParam String username) throws Exception {
        return myUserService.getUser(username);
    }
    @GetMapping("/getAll")
    public List<User> getAll(){
        return myUserService.getAll();
    }

    @GetMapping("/getUsers")
    public List<UserProjection> getUsers(){
        return myUserService.getAllUsers();
    }

    @GetMapping("/getAdmins")
    public List<UserProjection> getAdmins(){
        return myUserService.getAllAdmins();
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody Map<String,String> requestMap)  {
        try{
           return  myUserService.changePassword(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }

       return ("Something went wrong!");
    }

    // send an email
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody Map<String,String> requestMap){
        try {
            return myUserService.sendEmail(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "Something went wrong!";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestBody Map<String,String> requestMap){
        try {
            return myUserService.forgotPassword(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "Someting went wrong!";
    }

}
