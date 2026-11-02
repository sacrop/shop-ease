package com.shop.ease.model;

import java.text.DecimalFormat;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedProductFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedProductFilter", condition = "deleted = :isDeleted")
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    @NotBlank
    private String productName;
    
    @Column(length = 1000)
    private String productDescription;

    @Column(length = 1000)
    private String productSpecification;
    
    private List<String> imagepath;

    private double productPrice;

    @ManyToOne
    @JoinColumn(name = "cat_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcat_id")
    private Subcategory subcategory;

    private int stockQuantity;

    private boolean productDeleted = Boolean.FALSE;

    public double formattedPrice() {
        // Create a DecimalFormat object with the desired format
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        // Format the price using the DecimalFormat
        return Double.parseDouble(decimalFormat.format(productPrice));
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductSpecification() {
        return productSpecification;
    }

    public void setProductSpecification(String productSpecification) {
        this.productSpecification = productSpecification;
    }

    public List<String> getImagepath() {
        return imagepath;
    }

    public void setImagepath(List<String> imagepath) {
        this.imagepath = imagepath;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public boolean isProductDeleted() {
        return productDeleted;
    }

    public void setProductDeleted(boolean productDeleted) {
        this.productDeleted = productDeleted;
    }

    
}
