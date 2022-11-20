package com.brahim.controller;


import com.brahim.jwtUtil.JwtUtil;
import com.brahim.model.Role;
import com.brahim.model.User;
import com.brahim.repository.RoleRepository;
import com.brahim.service.MyUserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BasicController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    MyUserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    JwtUtil jwtUtil;
    @GetMapping("/hello")
    public String hello(){
        return "Hello,Brahim !! ";
    }


    @GetMapping("/forUser")
    @PreAuthorize("hasRole('USER')")
    public String forUser(){
        return "Only users are authorizes !!!";
    }


    @GetMapping("/forAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String forAdmin(){
        return "Only Admin is authorized  !!!";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) throws Exception{
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword());
        authenticationManager.authenticate(token);
        String jwtToken = jwtUtil.generate(loginDTO.getEmail());
        return jwtToken;
    }
    // search all users

    // signup
    @PostMapping("/signup")
    public String signUp(@RequestBody Map<String,String> requestMap) throws Exception {
        //call  validate requestMap
        if(validateRequestMap(requestMap)){
         User userdb = userService.getUser(requestMap.get("email"));
             if(userdb == null ){
                userService.createUser(getDataFromRequestMap(requestMap)) ;
                return "User added successfully.";
             }else {
                 return "This email already exist";
             }
        }else {
            return "your data are invalide !!";
        }

        //return "Something went wrong!";
    }

    public Boolean validateRequestMap(Map<String,String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contact")
        && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }
        return false;
    }

    public User getDataFromRequestMap(Map<String,String> requestMap) throws Exception {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContact(requestMap.get("contact"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("ok");
        Role role = roleRepository.findByName("ROLE_USER");
        user.getRoles().add(role);

       return user;
    }
}
@Data
 class LoginDTO {
    private String  email;
    private String password;
 }