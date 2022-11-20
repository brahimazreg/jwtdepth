package com.brahim.controller;


import com.brahim.model.Product;
import com.brahim.projection.ProductProjection;
import com.brahim.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    private MyUserService myUserService;

    @PostMapping("/addProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String addProduct(@RequestBody Map<String,String> requestMap){
        try {
            return myUserService.addProduct(requestMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    // get all
    @GetMapping("/products")
    public List<ProductProjection> getAllProducts(){
        try {
            return myUserService.getAllProduct();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    // find a product by name
    @GetMapping("/getProductsByCategory")
    public List<ProductProjection> getProductByCategory(@RequestParam String categoryName){
        try{
            return myUserService.getProductByCategory(categoryName);
        }catch (Exception e) {
            e.printStackTrace();
      }
       return  null;
    }

    // update product only admin can do
    @PutMapping("/updateProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateProduct(@RequestBody Map<String,String> requestMap)  {
        try{
            return myUserService.updateProduct(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "Something went wrong !!";
    }

  // delete product only admin can do
    @PostMapping("/deleteProduct/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProduct(@PathVariable Integer id){
       try {
           return myUserService.deleteProduct(id);
       }catch (Exception e){
           e.printStackTrace();
       }
        return "Something went wrong!";
    }

  // update status  only admin can do
  @PutMapping("/updateStatus")
  @PreAuthorize("hasRole('ADMIN')")
  public String updateStatus(@RequestBody Map<String,String> requestMap){
      try {
          return myUserService.updateStatus(requestMap);
      }catch (Exception e){
          e.printStackTrace();
      }
      return "Something went wrong!";
  }

  // find product by id
    @GetMapping("/getProduct/{id}")
    public ProductProjection getProductById(@PathVariable Integer id){
        try{
            return myUserService.getProductById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ProductProjection();
    }

}
