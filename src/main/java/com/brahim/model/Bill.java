package com.brahim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bills")
public class Bill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uuid;
    private String email;
    private String contact;
    @Column(name = "paymentmethod")
    private String paymentMethod;
    private Double total;
    //private Product[] productDetails;
    private String productDetails;
    @Column(name = "createdby")
    private String createdBy;
}
