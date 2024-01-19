package com.shop.ease.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.shop.ease.Repository.CartItemRepository;
import com.shop.ease.Repository.CartRepository;
import com.shop.ease.model.Cart;
import com.shop.ease.model.CartItem;
import com.shop.ease.model.Product;
import com.shop.ease.model.UserDtls;
@Service
public class ShoppingCartService {

    
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired 
    private CartRepository cartRepository;

    

    public boolean isProductExist(Product product){
        return cartItemRepository.existsByProduct(product);
    }

    // public List<CartItem> getallCartItem(){
    //     return cartItemRepository.findAll();
    // }

    public CartItem findbyUserProduct(UserDtls user, Product product) {
    
        return cartItemRepository.findByUserDtlsAndProduct(user, product);
    }

    public void saveCartItem(CartItem item){
        cartItemRepository.save(item);
    }

    public CartItem getCartItemById(long id)
    {
        return cartItemRepository.findById(id);

    }

    public List<CartItem> getCartItemByUserDtls(UserDtls user){
        return cartItemRepository.findByUserDtls(user);
    }

    public void saveCart(Cart cart) {
         cartRepository.save(cart);
    }

    public Cart getCartByUserId(int id) {
        return cartRepository.findCartByUserId(id);
    }

    public void deleteCartById(long id){
        cartItemRepository.deleteById(id);
    }
    public void removeCartByCartItem(CartItem cartItem){
        cartItemRepository.deleteById(cartItem.getId());
    }

    public void clearCart(UserDtls user) {
        List<CartItem> cartItems = getCartItemByUserDtls(user);

        for (CartItem cartItem : cartItems) {
            removeCartByCartItem(cartItem);;
        }
    }

    public void UpdateStockItem(Cart cart) {
        List<CartItem> cartlist=cart.getItems();
        for(CartItem cartItem:cartlist){
            int stock=cartItem.getProduct().getStockQuantity()-cartItem.getQuantity();
            cartItem.getProduct().setStockQuantity(stock);
            saveCartItem(cartItem);
        }
        cart.setItems(cartlist);
        saveCart(cart);

    }

    
}
