package com.shop.ease.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.WishlistRepository;
import com.shop.ease.helper.MycustomException;
import com.shop.ease.model.Product;
import com.shop.ease.model.UserDtls;
import com.shop.ease.model.Wishlist;

@Service
public class WishlistService {
    
    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductService productService;

    public void addToWishList(UserDtls user, Long id) throws MycustomException {
        Wishlist wishlist = getWishlist(user);
        Product product = productService.getproductbyid(id);
    
        // Check if the product already exists in the wishlist
        if (wishlist.getProducts() != null && wishlist.getProducts().contains(product)) {
            // Product already exists in the wishlist, handle accordingly (e.g., throw an exception, return early)
            // Here, I'm throwing an exception for demonstration purposes
            throw new MycustomException("Product already exists in the wishlist");
        }
    
        List<Product> productList = wishlist.getProducts();
        if (productList == null) {
            productList = new ArrayList<>();
        }
        productList.add(product);
        wishlist.setProducts(productList);
        saveWishList(wishlist);
    }

    public Wishlist getWishlist(UserDtls user){
            Wishlist wishlist=wishlistRepository.findByUser(user);
            if(wishlist==null){
                wishlist=new Wishlist();
                wishlist.setUser(user);
                saveWishList(wishlist);
            }
            return wishlist;
    }

    public void saveWishList(Wishlist wishlist){
        wishlistRepository.save(wishlist);
    }

    public List<Product> getAllProductFromWishList(UserDtls user){
        Wishlist wishlist=getWishlist(user);

        List<Product> productlist=wishlist.getProducts();

        return productlist;
    }

    public void removeProduct(UserDtls user, Long id) {
       Wishlist wishlist= getWishlist(user);
       Product product=productService.getproductbyid(id);
       wishlist.getProducts().remove(product);
       saveWishList(wishlist);
    }

    
}
