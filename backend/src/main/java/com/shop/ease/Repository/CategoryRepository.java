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

  public boolean existsByCatname(String name);

  public Category findByCatname(String catname);

  @Query("SELECT C FROM Category C")
  public List<Category> findAllCategory();

  @Query("SELECT s FROM Subcategory s WHERE s.category.catid = :categoryId")
  List<Subcategory> findSubcategoryByCategoryId(@Param("categoryId") int categoryId);

  @Query("SELECT new map(s.id as id, s.subcatname as subcatname) FROM Subcategory s WHERE s.category.catid = :categoryId")
  List<Map<String, Object>> findSubcategoryNamesAndIdsByCategoryId(@Param("categoryId") int categoryId);

  @Query("SELECT c.catname from Category c")
  public List<String> getAllCategoryName();

  public Category findByCatid(int catid);

  public boolean existsByCatnameAndDeleted(String catname, boolean deleted);

  public List<Category> findByDeletedFalse();

}
