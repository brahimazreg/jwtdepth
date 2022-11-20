package com.brahim.controller;

import com.brahim.model.Role;
import com.brahim.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    MyUserService myUserService;
    @GetMapping("/roles")
    public List<Role> getRoles(){
        return myUserService.getRoles();
    }
}
