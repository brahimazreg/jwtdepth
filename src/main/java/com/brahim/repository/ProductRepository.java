package com.brahim.repository;


import com.brahim.model.Product;
import com.brahim.projection.ProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
      Product findByName(String productName) ;

    @Query("select new com.brahim.projection.ProductProjection(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p  where p.id=:id")
      ProductProjection searchById(@Param("id") Integer id);

    //@Query("select new com.brahim.projection.ProductProjection(p.id,p.name,c.id,p.description,p.price,p.status) from Product p join p.category c")
    @Query("select new com.brahim.projection.ProductProjection(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p ")
    List<ProductProjection> getAllProducts();
    //@Query("select new com.brahim.projection.ProductProjection(p.id,p.name,c.id,p.description,p.price,p.status) from Product p join p.category c where c.name=:categoryName")
    @Query("select new com.brahim.projection.ProductProjection(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p  where p.category.name=:categoryName")
    List<ProductProjection> getAllProductsByCategory(@Param("categoryName") String categoryName);
   /* @Query("update products set status=:status where id=:id")
    String updateStatus(@Param("status") String status, @Param("id") Integer id);*/
}
