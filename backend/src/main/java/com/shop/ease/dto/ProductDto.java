// product model is created here
package com.shop.ease.dto;

import java.util.List;

public class ProductDto {
    private long productId;
    private String productName;
    private String productDescription;
    private List<String> imagePath;
    private double productPrice;
    private int categoryId;
    private int subcategoryId;
    private int stockQuantity;

    private ProductDto(Builder builder) {
        this.productId = builder.productId;
        this.productName = builder.productName;
        this.productDescription = builder.productDescription;
        this.imagePath = builder.imagePath;
        this.productPrice = builder.productPrice;
        this.categoryId = builder.categoryId;
        this.subcategoryId = builder.subcategoryId;
        this.stockQuantity = builder.stockQuantity;
    }

    public static class Builder {
        private long productId;
        private String productName;
        private String productDescription;
        private List<String> imagePath;
        private double productPrice;
        private int categoryId;
        private int subcategoryId;
        private int stockQuantity;

        public Builder setProductId(long productId) {
            this.productId = productId;
            return this;
        }

        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder setProductDescription(String productDescription) {
            this.productDescription = productDescription;
            return this;
        }

        public Builder setImagePath(List<String> imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Builder setProductPrice(double productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder setCategoryId(int categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder setSubcategoryId(int subcategoryId) {
            this.subcategoryId = subcategoryId;
            return this;
        }

        public Builder setStockQuantity(int stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public ProductDto build() {
            return new ProductDto(this);
        }
    }
}
