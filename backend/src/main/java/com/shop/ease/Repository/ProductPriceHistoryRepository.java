package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Product;
import com.shop.ease.model.ProductPriceHistory;
@Repository
public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistory,Integer> {

    public ProductPriceHistory findByProduct(Product product);

    public boolean existsByProduct(Product product);
    
}
