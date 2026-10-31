package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Product;
import com.shop.ease.model.Review;
import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {

    public List<Review> findByProduct(Product product);
    
}
