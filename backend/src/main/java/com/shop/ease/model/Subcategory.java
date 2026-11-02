package com.shop.ease.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@SQLDelete(sql = "UPDATE product_subcategory SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedSubCategoryFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedSubCategoryFilter", condition = "deleted = :isDeleted")
@Table(name = "product_subcategory")
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subcategoryId;
    private String subcategoryName;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "subcategory")
    private List<Product> products=new ArrayList<>();

     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
     @JsonIgnore
    private Category category;

     private boolean deleted = Boolean.FALSE;


    public String toString() {
        return "Subcategory{" +
                "subcategoryId=" + subcategoryId +
                ", subcategoryName='" + subcategoryName + '\'' +
                '}';
    }


    public int getSubcategoryId() {
        return subcategoryId;
    }


    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }


    public String getSubcategoryName() {
        return subcategoryName;
    }


    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }


    public List<Product> getProducts() {
        return products;
    }


    public void setProducts(List<Product> products) {
        this.products = products;
    }


    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }


    public boolean isDeleted() {
        return deleted;
    }


    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    
}
