package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import com.shop.ease.model.Subcategory;
import com.shop.ease.model.Category;


@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {

    public boolean existsBySubcategoryName(String name);

    public Subcategory findBySubcategoryName(String name);

    public Subcategory findBySubcategoryId(int id);

    public void deleteByCategory(Category category);

    public boolean existsBySubcategoryNameAndCategory_CategoryName(String subcategoryName, String categoryName);

}
