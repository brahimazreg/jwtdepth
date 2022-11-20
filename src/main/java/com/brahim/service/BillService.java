package com.brahim.service;


import com.brahim.Utils;
import com.brahim.model.Bill;
import com.brahim.model.User;
import com.brahim.repository.BillRepository;
import com.brahim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BillService {
    @Autowired
    BillRepository billRepository;
    @Autowired
    UserRepository userRepository;

    public String generateBill(Map<String, Object> requestMap) {
        try {
            if(validateBill(requestMap)){
              String fileName = Utils.generateUUID();
              requestMap.put("uuid",fileName);
              insertBill(requestMap);
              return fileName;
            }else {
                return "Invalid Data";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Something went wrong!";
    }

    private void insertBill(Map<String, Object> requestMap) {
      Bill bill = new Bill();
      User userdb = userRepository.findByEmail((String) requestMap.get("email"));
        System.out.println("------------------");
      System.out.println(userdb.getName());
        if(userdb != null) {
            bill.setUuid((String)requestMap.get("uuid"));
            bill.setContact((String)requestMap.get("contact"));
            bill.setEmail((String)requestMap.get("email"));
            bill.setTotal(Double.parseDouble((String)requestMap.get("total")));
            bill.setPaymentMethod((String)requestMap.get("paymentMethod"));
            bill.setCreatedBy(userdb.getName());
            bill.setProductDetails((String)requestMap.get("productDetails"));
            billRepository.save(bill);
        }else {
            throw new UsernameNotFoundException("User does't exist");
        }

    }


    // validate data
    public Boolean validateBill(Map<String,Object> requestMap){
        if(requestMap.containsKey("email") && requestMap.containsKey("contact") &&
           requestMap.containsKey("paymentMethod") && requestMap.containsKey("total") &&
           requestMap.containsKey("productDetails") && requestMap.containsKey("name")){
            return true;
        }else {
            return false;
        }
    }
}
