package com.brahim.projection;

import com.brahim.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductProjection {

    private Integer id;
    private String name;
    private String description;
    private double price;
    private String status;
    private Integer categoryId;
    private String categoryName;

    public ProductProjection(Integer id){
        this.id=id;
    }
}
