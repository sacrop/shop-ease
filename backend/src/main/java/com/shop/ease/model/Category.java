package com.shop.ease.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;

import jakarta.persistence.Table;

@Entity
@SQLDelete(sql = "UPDATE product_category SET deleted = true WHERE categoryId = ?")
@FilterDef(name = "deletedCategoryFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedCategoryFilter", condition = "deleted = :isDeleted")
@Table(name = "product_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    @Column(unique = true)
    private String categoryName;

    private String imagePath;

    private String iconClass;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "category")
    private List<Subcategory> subcategory=new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "category")
    private List<Product> product=new ArrayList<>();


    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

    private boolean deleted = Boolean.FALSE;


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public List<Subcategory> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<Subcategory> subcategory) {
        this.subcategory = subcategory;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    
}
