package com.shop.ease.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table
public class ProductPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productPriceHistoryId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double productOriginalPrice;

    private LocalDateTime changeDate;

    public int getProductPriceHistoryId() {
        return productPriceHistoryId;
    }

    public void setProductPriceHistoryId(int productPriceHistoryId) {
        this.productPriceHistoryId = productPriceHistoryId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getProductOriginalPrice() {
        return productOriginalPrice;
    }

    public void setProductOriginalPrice(double productOriginalPrice) {
        this.productOriginalPrice = productOriginalPrice;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    
}
