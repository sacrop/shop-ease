package com.shop.ease.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Category;
import com.shop.ease.model.Subcategory;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

  public boolean existsBycategoryName(String categoryName);

  public Category findBycategoryName(String categoryName);

  @Query("SELECT C FROM Category C")
  public List<Category> findAllCategory();

  @Query("SELECT s FROM Subcategory s WHERE s.category.categoryId = :categoryId")
  List<Subcategory> findSubcategoryByCategoryId(@Param("categoryId") int categoryId);

  @Query("SELECT new map(s.subcategoryId as id, s.subcategoryName as subcatname) FROM Subcategory s WHERE s.category.categoryId = :categoryId")
  List<Map<String, Object>> findSubcategoryNamesAndIdsByCategoryId(@Param("categoryId") int categoryId);

  @Query("SELECT c.categoryName from Category c")
  public List<String> getAllCategoryName();

  public Category findByCategoryId(int categoryId);

  public boolean existsBycategoryNameAndDeleted(String categoryName, boolean deleted);

  public List<Category> findByDeletedFalse();

}
