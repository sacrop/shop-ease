package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ease.model.UserDtls;
import com.shop.ease.model.Wishlist;



public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    public Wishlist findByUser(UserDtls user);
}
