package com.shop.ease.dto;

import java.util.List;

import lombok.Data;

@Data
public class SubcategoryDto {
    private int id;
    private String subcatname;
    private List<ProductDto> products;
    private List<CategoryDto> category;
}
