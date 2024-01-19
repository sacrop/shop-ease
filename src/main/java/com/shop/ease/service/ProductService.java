package com.shop.ease.service;

import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.ProductRepository;
import com.shop.ease.dto.ProductDto;

import com.shop.ease.model.Product;

import jakarta.persistence.EntityManager;

@Service
public class ProductService {

    @Autowired
    private ProductRepository prorepo;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    public List<Product> getallproduct() {
        return prorepo.findAll();
    }

    public List<Product> getNotDeletedProducts() {
        return prorepo.findByDeletedFalse();
    }

    public void createproduct(Product product) {
        prorepo.save(product);
    }

    public void deleteproductbyid(long id) {

        prorepo.deleteById(id);
    }

    public Product getproductbyid(long l) {
        return prorepo.findById(l);
    }

    public void saveproduct(Product product) {
        prorepo.save(product);
    }

    public List<Product> getProductsBypname(String pname) {
        return prorepo.findByPname(pname);
    }

    public int getproductcount() {
        return prorepo.countproduct();
    }

    public Product convertProductDtoProduct(ProductDto productDto) {
        Product product = new Product();
        product = modelMapper.map(productDto, Product.class);
        return product;
    }

    public List<Product> getdeletedProduct() {
        Boolean isDeleted = true;
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedProductFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<Product> pro = prorepo.findAll();
        session.disableFilter("deletedProductFilter");
        return pro;

    }

    public void restoredeleted() {
        List<Product> pro = this.getdeletedProduct();
        for (Product prod : pro) {
            prod.setDeleted(false);
            prorepo.save(prod);
        }
    }

    public Page<Product> getProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return prorepo.findAll(pageable);
    }

}
