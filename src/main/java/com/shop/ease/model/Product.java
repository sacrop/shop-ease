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
import lombok.Data;
@Entity
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedProductFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedProductFilter", condition = "deleted = :isDeleted")
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String pname;
    
    @Column(length = 1000)
    private String pdescription;

    @Column(length = 1000)
    private String productSpecification;
    
    private List<String> imagepath;

    private double price;

    @ManyToOne
    @JoinColumn(name = "cat_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcat_id")
    private Subcategory subcategory;

    private int stockQuantity;

    private boolean deleted = Boolean.FALSE;

    public double formattedPrice() {
        // Create a DecimalFormat object with the desired format
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        // Format the price using the DecimalFormat
        return Double.parseDouble(decimalFormat.format(price));
    }


    
}
