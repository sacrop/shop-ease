package com.shop.ease.service;

import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
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

    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;

    public ProductService(
        ProductRepository productRepository,
        EntityManager entityManager,
        ModelMapper modelMapper
    ) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
        this.modelMapper = modelMapper;
    }

    public List<Product> getallproduct() {
        return productRepository.findAll();
    }

    public List<Product> getNotDeletedProducts() {
        return productRepository.findByProductDeletedFalse();
    }

    public void createproduct(Product product) {
        productRepository.save(product);
    }

    public void deleteproductbyid(long id) {

        productRepository.deleteById(id);
    }

    public Product getproductbyid(long l) {
        return productRepository.findByProductId(l);
    }

    public void saveproduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> getProductsBypname(String pname) {
        return productRepository.findByProductName(pname);
    }

    public int getproductcount() {
        return productRepository.countProducts();
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
        List<Product> pro = productRepository.findAll();
        session.disableFilter("deletedProductFilter");
        return pro;

    }

    public void restoredeleted() {
        List<Product> pro = this.getdeletedProduct();
        for (Product prod : pro) {
            prod.setProductDeleted(false);
            productRepository.save(prod);
        }
    }

    public Page<Product> getProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

}
