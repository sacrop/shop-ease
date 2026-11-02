package com.shop.ease.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUserDetails userDtls;

    private double totalamount;

    public double getTotalamount() {
        totalamount=0.0;
        for(CartItem item:items){
            totalamount+=(item.getProduct().getProductPrice()*item.getQuantity());
            
        }
        return totalamount;
    }

    @Override
    public String toString() {
        return "Cart [id=" + id + ", totalamount=" + totalamount + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public MyUserDetails getUserDtls() {
        return userDtls;
    }

    public void setUserDtls(MyUserDetails userDtls) {
        this.userDtls = userDtls;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    

   
}
