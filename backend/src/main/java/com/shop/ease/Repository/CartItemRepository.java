package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.CartItem;
import com.shop.ease.model.Product;
import com.shop.ease.model.UserDtls;

import java.util.List;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    
    public boolean existsByProduct(Product product);

    public List<CartItem> findByUserDtls(UserDtls userDtls);

    public CartItem findByUserDtlsAndProduct(UserDtls user, Product product);

    public CartItem findById(long id);

    

    
}
