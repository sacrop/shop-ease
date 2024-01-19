package com.shop.ease.dto;



import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDto {
    @NotNull
    private int catid;
    @NotNull
    private String catname;
    
    private List<SubcategoryDto> subcatory;
    private List<ProductDto> products;
    
}
