package com.shop.ease.dto;

import java.util.List;

public class SubcategoryDto {
    private int subcategoryId;
    private String subcategoryName;
    private List<ProductDto> products;
    private List<CategoryDto> categories;

    private SubcategoryDto(Builder builder) {
        this.subcategoryId = builder.subcategoryId;
        this.subcategoryName = builder.subcategoryName;
        this.products = builder.products;
        this.categories = builder.categories;
    }

    public static class Builder {
        private int subcategoryId;
        private String subcategoryName;
        private List<ProductDto> products;
        private List<CategoryDto> categories;

        public Builder setSubcategoryId(int subcategoryId) {
            this.subcategoryId = subcategoryId;
            return this;
        }

        public Builder setSubcategoryName(String subcategoryName) {
            this.subcategoryName = subcategoryName;
            return this;
        }

        public Builder setProducts(List<ProductDto> products) {
            this.products = products;
            return this;
        }

        public Builder setCategories(List<CategoryDto> categories) {
            this.categories = categories;
            return this;
        }

        public SubcategoryDto build() {
            return new SubcategoryDto(this);
        }
    }
}
