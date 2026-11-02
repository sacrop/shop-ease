package com.shop.ease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.shop.ease.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{
    
    public Product findByProductId(long id);

    public List<Product> findByProductName(String productName);

    @Query("SELECT count(*) FROM Product")
    public int countProducts();

    public void deleteByProductId(Long id);

    public List<Product> findByProductDeletedFalse();

    public List<Product> getByProductName(String productName);
     

    
}
