package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ease.model.MyUserDetails;
import com.shop.ease.model.Wishlist;



public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    public Wishlist findByMyUserDetails(MyUserDetails user);
}
