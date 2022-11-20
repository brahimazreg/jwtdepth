package com.brahim.service;

import com.brahim.jwtFilter.JwtTokenFilter;
import com.brahim.model.Category;
import com.brahim.model.Product;
import com.brahim.model.Role;
import com.brahim.model.User;
import com.brahim.projection.ProductProjection;
import com.brahim.projection.Test;
import com.brahim.projection.UserProjection;
import com.brahim.repository.CategoryRepository;
import com.brahim.repository.ProductRepository;
import com.brahim.repository.RoleRepository;
import com.brahim.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.*;

@Service @Transactional
@Slf4j
public class MyUserService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Lazy
    @Autowired
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    ProductRepository productRepository;

    // add a user
    public User createUser(User user) {
        String pw = user.getPassword();
        user.setPassword(passwordEncoder.encode(pw));
        return userRepository.save(user);
    }

    // add a role
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    // add role to a user
    public void addRoleToUser(String email, String roleName) {
        User userdb = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        System.out.println(userdb.getEmail());
        System.out.println(role);
        userdb.getRoles().add(role);

    }

    // get a user
    public User getUser(String email) throws Exception {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            throw new Exception("User doesn't exist");
        }
    }

    // get all users
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<UserProjection> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public List<UserProjection> getAllAdmins() {
        return userRepository.getAllAdmins();
    }

    // change password
    public String changePassword(Map<String, String> requestMap) {

        try {
            String currentEmail = jwtTokenFilter.getCurrentUser();
            String pw = passwordEncoder.encode(requestMap.get("oldPassword"));
            User userdb = userRepository.findByEmail(jwtTokenFilter.getCurrentUser());
            //System.out.println(currentEmail);
            //System.out.println(pw);
            //System.out.println(userdb.getEmail());
            //System.out.println(requestMap.get("email"));
            if (userdb != null ){
                if(userdb.getPassword().equals(requestMap.get("oldPassword"))){
                    if(currentEmail == requestMap.get("email")){
                        userdb.setPassword(requestMap.get("newPassword"));
                        userRepository.save(userdb);
                        return "Password has been changed successfully";
                    }return "the email is incorrect!";

                } return "The old password is incorrect!";
            } return "Something went wrong !";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Something went wrong !";
    }

    // send email
    public String sendEmail(Map<String,String> requestMap){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(requestMap.get("from"));
        message.setTo(requestMap.get("to"));
        message.setSubject(requestMap.get("subject"));
        message.setText(requestMap.get("body"));

        mailSender.send(message);
        return ("Mail sent successfully !!");
    }

    public String addCategory(Category category) {
        try {
            if( category.getName() != null  && !category.getName().isEmpty()){
              categoryRepository.save(category);
              return "A new category has been added !";
            }
            return "Category must be not null";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    public List<Category> getAllCategories() {
        try{
            return categoryRepository.findAll();

        }catch (Exception e){
            e.printStackTrace();
        }
        return  new ArrayList<>();
    }

    public Category getCategoryByName(String categoryName) {
        try{
            return categoryRepository.findByName(categoryName);

        }catch (Exception e){
            e.printStackTrace();
        }
        return  new Category();

    }

    // forgot password
    public String forgotPassword(Map<String,String> requestMap){
        try {
            User userdb = userRepository.findByEmail(requestMap.get("email"));
            if(userdb != null ){
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message,true);
                helper.setFrom("brahimaz@gmail.com");
                helper.setTo(requestMap.get("email"));
                helper.setSubject("Reset password!");
                String helpMsg ="String htmlMsg = \"<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> \" + to + \" <br><b>Password: </b> \" + password + \"<br><a href=\\\"http://localhost:4200/\\\">Click here to login</a></p>\";";
                message.setContent(helpMsg,"text/html");
              /*  SimpleMailMessage message = new SimpleMailMessage();
                String helpMsg ="String htmlMsg = \"<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> \" + to + \" <br><b>Password: </b> \" + password + \"<br><a href=\\\"http://localhost:4200/\\\">Click here to login</a></p>\";";
                message.setFrom("brahimaz@gmail.com");
                message.setTo(requestMap.get("email"));
                message.setSubject("Reset password");
                message.setText(helpMsg);*/



                mailSender.send(message);
                return "Check your email ";
            }else {
                return "Email  doesn't exist!";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "Something went wrong!";
    }

    public String updateCategory(Map<String, String> requestMap) {
        if(validateUpdateCategory(requestMap)){
            Category category =categoryRepository.findById(Integer.parseInt(requestMap.get("id"))).get();
            if(category != null){
                categoryRepository.save(getCategoryFromRequestMap(requestMap));
                return "Category updated successfuly";
            }else {
                return "category doesn't exist !";
            }
        }else {
            return "Category not valide!!";
        }

    }
    public Boolean validateUpdateCategory(Map<String, String> requestMap){
        if(requestMap.get("id") != null  && requestMap.get("name") != null){
            return true;
        }else {
            return false;
        }
    }

    // get category from requestMap

    public Category getCategoryFromRequestMap(Map<String, String> requestMap){
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("id")));
        category.setName(requestMap.get("name"));
        return category;
    }
     // add product
    public String addProduct(Map<String, String> requestMap) {
        try {
            if(validateNewProduct(requestMap)){
               productRepository.save(getProductFromRequestMap(requestMap)) ;
               return "Product added Successfully !";
            }else {
                return "Invalid Data !";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "Something went wrong!";
    }

    // validate the new product
    public Boolean validateNewProduct(Map<String,String> requestMap){
        if( requestMap.containsKey("categoryId") && requestMap.containsKey("name") &&
            requestMap.containsKey("price") && requestMap.containsKey("description") && requestMap.containsKey("status")){
            return true;
        }else {
            return false;
        }
    }
    // get product from requestMap
    public Product getProductFromRequestMap(Map<String,String> requestMap){
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        Product product = new Product();
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setPrice(Double.parseDouble(requestMap.get("price")));
        product.setDescription(requestMap.get("description"));
        product.setStatus("true");
        return product;
    }

    // get all products
    public List<ProductProjection> getAllProduct() {
        try {
            return productRepository.getAllProducts();

        }catch (Exception e){
            e.printStackTrace();
        }
            return new ArrayList<>();
    }

   /* public Product getProductByName(String name) {
        try{
            return productRepository.findByName(name);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }*/

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public List<ProductProjection> getProductByCategory(String categoryName) {
        try{
            return productRepository.getAllProductsByCategory(categoryName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    // update product
    public String updateProduct(Map<String, String> requestMap) {
        try{
           if ( validateRequestMap( requestMap)){
               Product productdb = retreiveDataFromRequestMap( requestMap);
               if(productdb != null){
                   productRepository.save(retreiveDataFromRequestMap( requestMap));
                   return "Product updated successfully";
               }else {
                   return "Product doesn't exist !!";
               }
           }else {
               return "invalid data";
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "Something went wrong !";
    }
    // validate data in requestMap
    public Boolean validateRequestMap(Map<String,String> requestMap){
        if(requestMap.containsKey("id") && requestMap.containsKey("name") && requestMap.containsKey("description")
           && requestMap.containsKey("price")  && requestMap.containsKey("status")
           && requestMap.containsKey("categoryName")){
            return true;
        }else {
            return false;
        }
    }

    // retreive data for updte from requestMap
    public Product retreiveDataFromRequestMap(Map<String,String> requestMap){
        Category category = categoryRepository.findByName(requestMap.get("categoryName"));
        Product updatedProduct = productRepository.findById(Integer.parseInt(requestMap.get("id"))).get();
        if(category != null){
            updatedProduct.setCategory(category);
            updatedProduct.setName(requestMap.get("name"));
            updatedProduct.setStatus(requestMap.get("status"));
            updatedProduct.setDescription(requestMap.get("description"));
            updatedProduct.setPrice(Double.parseDouble(requestMap.get("price")));
            return updatedProduct;
        }
        return null;
    }
     // delete product
    public String deleteProduct(Integer id) {
        try {
            Product deletedProduct = productRepository.findById(id).get();
            productRepository.delete(deletedProduct);
            return "A product deleted successfully";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Something went wrong!";
    }
      // updte status for a product
    public String updateStatus(Map<String,String> requestMap) {
        try {
            if(validateUpdateStatus( requestMap)){
                Product product = retreiveDataForStatus( requestMap);
                if( product != null){
                    productRepository.save(product);
                    return "Status updated successfully";
                }
            }else {
                return "Invalid Data";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "Something went wrong!";
    }
    // validate requestMap for update status
    public Boolean validateUpdateStatus(Map<String,String> requestMap){
        if(requestMap.containsKey("id") && requestMap.containsKey("status")){
            return true;
        }
        else {return false;}
    }
    // rereive data from requestMap
    public Product retreiveDataForStatus(Map<String,String> requestMap){
        Product product = productRepository.findById(Integer.parseInt(requestMap.get("id"))).get();
        if(product != null){
            product.setStatus(requestMap.get("status"));
            return product;
        }else {
            return null;
        }

    }

    public ProductProjection getProductById(Integer id) {
        try{
            ProductProjection product = productRepository.searchById(id);
            return product;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
