package com.shop.ease.service;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.ProductPriceHistoryRepository;
import com.shop.ease.model.Product;
import com.shop.ease.model.ProductPriceHistory;

@Service
public class ProductPriceHistoryService {
    
    @Autowired
    private ProductPriceHistoryRepository priceHistoryRepository;

    // @Autowired 
    // priv

    public ProductPriceHistory  getOriginalPriceOfProduct(Product product ){
        return priceHistoryRepository.findByProduct(product);
    }

    public List<ProductPriceHistory> getPriceHistories(){
        return priceHistoryRepository.findAll();
    }

    public void updatePriceHistory(Product product) {
        ProductPriceHistory priceHistory=priceHistoryRepository.findByProduct(product);
        priceHistory.setProduct(product);
        priceHistory.setOriginalPrice(product.getPrice());
        priceHistory.setChangeDate(LocalDateTime.now());
        priceHistoryRepository.save(priceHistory);
    }
}
