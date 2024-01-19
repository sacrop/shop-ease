package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Cart;
@Repository
public interface CartRepository extends JpaRepository<Cart,Integer>{

    @Query("SELECT c FROM Cart c WHERE c.userDtls.id = :userId")
    public Cart findCartByUserId(@Param("userId") int userId);
    
}
