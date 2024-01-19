package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Category;
import com.shop.ease.model.Offer;




@Repository
public interface OfferRepository extends JpaRepository<Offer,Integer> {

    
    public Offer findById(int id);
    
    public boolean existsByCategory(Category category);

    public Offer findByCategory(Category category);
     
}
