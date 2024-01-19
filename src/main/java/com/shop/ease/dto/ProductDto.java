// product model is created here
package com.shop.ease.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductDto {
    private long id;
    private String pname;
    private String pdescription;
    private List<String> imagepath;
    private double price;
    private int categoryId;
    private int subcategoryId;
    private int stockQuantity;
}
