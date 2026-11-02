package com.shop.ease.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class ProductOffer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productOfferId;
    
    private double productOfferDiscount;

    @OneToOne
    private Product product;


    private LocalDateTime localDateTime;

    private String productOfferDescription;

    public int getProductOfferId() {
        return productOfferId;
    }

    public void setProductOfferId(int productOfferId) {
        this.productOfferId = productOfferId;
    }

    public double getProductOfferDiscount() {
        return productOfferDiscount;
    }

    public void setProductOfferDiscount(double productOfferDiscount) {
        this.productOfferDiscount = productOfferDiscount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getProductOfferDescription() {
        return productOfferDescription;
    }

    public void setProductOfferDescription(String productOfferDescription) {
        this.productOfferDescription = productOfferDescription;
    }

    
}
