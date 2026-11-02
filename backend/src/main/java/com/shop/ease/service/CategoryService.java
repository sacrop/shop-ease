package com.shop.ease.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.CategoryRepository;
import com.shop.ease.Repository.SubcategoryRepository;
import com.shop.ease.dto.CategoryDto;

import com.shop.ease.dto.SubcategoryDto;

import com.shop.ease.model.Category;

import com.shop.ease.model.Subcategory;



import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository catrepo;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SubcategoryRepository subcatrepo;

    public void createcat(Category cat) {
        catrepo.save(cat);
    }

    public void createsubcat(Subcategory subcat) {
        subcatrepo.save(subcat);
    }

    public List<Category> getallcategory()
    {
        return catrepo.findAllCategory();
    }

    public Boolean existsBycategoryName(String categoryName) {
    
            return catrepo.existsBycategoryName(categoryName);
        
    }  

    public List<Category> getdeletedCategories(Boolean isDeleted){
        Session session=entityManager.unwrap(Session.class);
        Filter filter=session.enableFilter("deletedCategoryFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<Category> cat=catrepo.findAll();
        session.disableFilter("deletedCategoryFilter");
        return cat ;
    }

    public List<Category> getNotdeletedCategories(){
        return catrepo.findByDeletedFalse();
    }
    

    public Boolean existsBySubcatname(String subcatname){
        return subcatrepo.existsBySubcategoryName(subcatname);
    }

    public Category getCategorybyname(String name){
        return catrepo.findBycategoryName(name);

    }

    public List<Subcategory> getallsubcategory() {
        return subcatrepo.findAll();
    }

    public Subcategory getSubcategorybyname(String name) {
        return subcatrepo.findBySubcategoryName(name);
    }

    public Category convertCategoryDtotCategory(CategoryDto categoryDto)
    {
        Category category=new Category();
     category=modelMapper.map(categoryDto,Category.class);
     return category;
    }
    
    public Subcategory convertSubcategoryDtoSubcategory(SubcategoryDto subcategoryDto){
        Subcategory subcategory=new Subcategory();
        subcategory=modelMapper.map(subcategoryDto, Subcategory.class);
        return subcategory;
    }

    public List<Map<String, Object>> getSubcategoryNamesIdsByCategoryId(int categoryId)
    {
        return catrepo.findSubcategoryNamesAndIdsByCategoryId(categoryId);
    }

     public List<Subcategory> getSubcategoriesByCategoryId(int categoryId)
    {
        return catrepo.findSubcategoryByCategoryId(categoryId);
    }

    public List<String> getAllCategoryNames()
    {
        return catrepo.getAllCategoryName();
    }

    public List<Subcategory> getSubcategoriesByCategoryName(String name) {
        Category category=getCategorybyname(name);
        if(category!=null){
            int catid=category.getCategoryId();
            return getSubcategoriesByCategoryId(catid);
        }
        return getallsubcategory();
    }

    //return category based on category id
    public Category getcategoryByid(int catid) {
        return catrepo.findByCategoryId(catid);
    }

    public Subcategory getSubcategoryById(int subcatid) {
        return subcatrepo.findBySubcategoryId(subcatid);
    }

    @Transactional
    public void deleteCategoryByid(int id) { 
        // subcatrepo.deleteByCategory(catrepo.findByCategoryId(id));
        catrepo.deleteById(id);
    }

    public void save(Category category) {
        catrepo.save(category);
    }

    public Boolean existBycategoryNameAndSubcategoryName(String subcatname, String categoryName) {
        return subcatrepo.existsBySubcategoryNameAndCategory_CategoryName(subcatname, categoryName);
    }
}
