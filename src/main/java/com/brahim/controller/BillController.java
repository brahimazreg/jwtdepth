package com.brahim.controller;


import com.brahim.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BillController {
    @Autowired
    BillService billService;

    @PostMapping("/generateBill")
    public String generateBillCode(@RequestBody Map<String,Object> requesMap){
        try {
            return billService.generateBill(requesMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "Something went wrong!";
    }
}
