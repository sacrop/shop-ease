package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ease.model.Product;
import com.shop.ease.model.ProductOffer;

public interface ProductOfferRepository extends JpaRepository<ProductOffer,Integer>{

    public ProductOffer findByProduct(Product product);

    public boolean existsByProduct(Product product);

    public ProductOffer findById(int id);
    
}
