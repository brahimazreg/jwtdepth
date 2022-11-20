package com.brahim.controller;

import com.brahim.model.Category;
import com.brahim.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {
    @Autowired
    MyUserService userService;

    @PostMapping("/addCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public String addCategory(@RequestBody Category category){
        try {
            return userService.addCategory(category);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "Unauthorized access !";
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories(){
        try {
            return userService.getAllCategories();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping("/getCategory")
    public Category getCategoryByName(@RequestParam String categoryName){
        try {
            return userService.getCategoryByName(categoryName);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new Category();
    }

    // update category

    @PutMapping("/updateCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateCategory(@RequestBody Map<String,String> requestMap){
        try {
            return userService.updateCategory(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Something went wrong";
    }
}
