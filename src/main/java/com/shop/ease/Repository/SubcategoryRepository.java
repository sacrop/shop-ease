package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import com.shop.ease.model.Subcategory;
import com.shop.ease.model.Category;


@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {

    public boolean existsBySubcatname(String name);

    public Subcategory findBySubcatname(String name);

    public Subcategory findById(int id);

    public void deleteByCategory(Category category);

    public boolean existsBySubcatnameAndCategory_Catname(String subcatname, String catname);

}
