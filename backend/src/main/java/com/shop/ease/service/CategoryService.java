package com.shop.ease.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
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

    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final SubcategoryRepository subcategoryRepository;
    
    public CategoryService(
        CategoryRepository categoryRepository,
        EntityManager entityManager,
        ModelMapper modelMapper,
        SubcategoryRepository subcategoryRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.entityManager = entityManager;
        this.modelMapper = modelMapper;
        this.subcategoryRepository = subcategoryRepository;
    }

    public void createcat(Category category) {
        categoryRepository.save(category);
    }

    public void createsubcat(Subcategory subcategory) {
        subcategoryRepository.save(subcategory);
    }

    public List<Category> getallcategory()
    {
        return categoryRepository.findAllCategory();
    }

    public Boolean existsByCategoryName(String categoryName) {
    
        return categoryRepository.existsBycategoryName(categoryName);
        
    }  

    public List<Category> getdeletedCategories(Boolean isDeleted){
        Session session=entityManager.unwrap(Session.class);
        Filter filter=session.enableFilter("deletedCategoryFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<Category> category=categoryRepository.findAll();
        session.disableFilter("deletedCategoryFilter");
        return category ;
    }

    public List<Category> getNotdeletedCategories(){
        return categoryRepository.findByDeletedFalse();
    }
    

    public Boolean existsBySubcatname(String subcategoryName) {
        return subcategoryRepository.existsBySubcategoryName(subcategoryName);
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findBycategoryName(categoryName);
    }

    public List<Subcategory> getallsubcategory() {
        return subcategoryRepository.findAll();
    }

    public Subcategory getSubcategoryByName(String subcategoryName) {
        return subcategoryRepository.findBySubcategoryName(subcategoryName);
    }

    public Category convertCategoryDtoToCategory(CategoryDto categoryDto)
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
        return categoryRepository.findSubcategoryNamesAndIdsByCategoryId(categoryId);
    }

     public List<Subcategory> getSubcategoriesByCategoryId(int categoryId)
    {
        return categoryRepository.findSubcategoryByCategoryId(categoryId);
    }

    public List<String> getAllCategoryNames()
    {
        return categoryRepository.getAllCategoryName();
    }

    public List<Subcategory> getSubcategoriesByCategoryName(String categoryName) {
        Category category=getCategoryByName(categoryName);
        if(category!=null){
            int catid=category.getCategoryId();
            return getSubcategoriesByCategoryId(catid);
        }
        return getallsubcategory();
    }

    //return category based on category id
    public Category getCategoryById(int categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }

    public Subcategory getSubcategoryById(int subcategoryId) {
        return subcategoryRepository.findBySubcategoryId(subcategoryId);
    }

    @Transactional
    public void deleteCategoryByid(int id) { 
        // subcatrepo.deleteByCategory(catrepo.findByCategoryId(id));
        categoryRepository.deleteById(id);
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public Boolean existBycategoryNameAndSubcategoryName(String subcategoryName, String categoryName) {
        return subcategoryRepository.existsBySubcategoryNameAndCategory_CategoryName(subcategoryName, categoryName);
    }
}
