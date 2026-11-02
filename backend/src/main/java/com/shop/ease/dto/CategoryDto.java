package com.shop.ease.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class CategoryDto {
    @NotNull
    private int categoryId;
    @NotNull
    private String categoryName;
    
    private List<SubcategoryDto> subcategoryDtos;
    private List<ProductDto> productDtos;

    private CategoryDto(Builder builder) {
        this.categoryId = builder.categoryId;
        this.categoryName = builder.categoryName;
        this.subcategoryDtos = builder.subcategoryDtos;
        this.productDtos = builder.productDtos;
    }

    public static class Builder {
        private int categoryId;
        private String categoryName;
        private List<SubcategoryDto> subcategoryDtos;
        private List<ProductDto> productDtos;

        public Builder setCategoryId(int categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder setCategoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Builder setSubcategoryDtos(List<SubcategoryDto> subcategoryDtos) {
            this.subcategoryDtos = subcategoryDtos;
            return this;
        }

        public Builder setProductDtos(List<ProductDto> productDtos) {
            this.productDtos = productDtos;
            return this;
        }

        public CategoryDto build() {
            return new CategoryDto(this);
        }
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<SubcategoryDto> getSubcategoryDtos() {
        return subcategoryDtos;
    }

    public List<ProductDto> getProductDtos() {
        return productDtos;
    }

    
    
}
