package com.shop.ease.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.OfferRepository;
import com.shop.ease.Repository.ProductOfferRepository;
import com.shop.ease.Repository.ProductPriceHistoryRepository;
import com.shop.ease.Repository.ProductRepository;
import com.shop.ease.model.Category;
import com.shop.ease.model.Offer;
import com.shop.ease.model.Product;
import com.shop.ease.model.ProductOffer;
import com.shop.ease.model.ProductPriceHistory;

import jakarta.transaction.Transactional;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ProductPriceHistoryRepository priceHistoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOfferRepository productOfferRepository;

    @Transactional
    public void createCategoryOffer(double discount, Category category, String description) {
        for (Product product : category.getProduct()) {
            double original = getHistoryPrice(product);
            double discountOffer = getDiscountOfferForProduct(product, discount);
            double discountedPrice = calculateDiscountedPrice(original, discountOffer);
            product.setPrice(discountedPrice);
            productRepository.save(product);
        }
        Offer offer = new Offer();
        offer.setCategory(category);
        offer.setDiscount(discount);
        offer.setOfferDescription(description);
        offerRepository.save(offer);
    }

    private double getHistoryPrice(Product product) {
        if (priceHistoryRepository.existsByProduct(product)) {
            ProductPriceHistory priceHistory = priceHistoryRepository.findByProduct(product);
            return priceHistory.getOriginalPrice();

        } else {
            ProductPriceHistory priceHistory = new ProductPriceHistory();
            priceHistory.setProduct(product);
            priceHistory.setChangeDate(LocalDateTime.now());
            priceHistory.setOriginalPrice(product.getPrice());
            priceHistoryRepository.save(priceHistory);
            return priceHistory.getOriginalPrice();
        }

    }

    private double calculateDiscountedPrice(double original, double discountOffer) {
        double rate = (original * discountOffer) / 100;
        return original - rate;
    }

    private double getDiscountOfferForProduct(Product product, double discount) {
        if (productOfferRepository.existsByProduct(product)) {
            ProductOffer productOffer = productOfferRepository.findByProduct(product);
            return productOffer.getDiscountOffer() + discount;
        } else {
            return discount;
        }
    }

    public List<Offer> getAllOffer() {
        return offerRepository.findAll();
    }

    public void deleteOffer(int id) {
        Offer offer = offerRepository.findById(id);
        Category category = offer.getCategory();
        for (Product pro : category.getProduct()) {
            if (offer.getDiscount() == getDiscountOfferForProduct(pro, offer.getDiscount())) {
                pro.setPrice(getHistoryPrice(pro));
            } else {
                double balanceDiscount = getDiscountOfferForProduct(pro, offer.getDiscount()) - offer.getDiscount();
                double price = calculateDiscountedPrice(getHistoryPrice(pro), balanceDiscount);
                pro.setPrice(price);
            }
            productRepository.save(pro);
        }
        offerRepository.delete(offer);
    }

    public boolean findExistOffer(Category category) {
        return offerRepository.existsByCategory(category);
    }

    public boolean findExistOffer(Product product) {
        return productOfferRepository.existsByProduct(product);
    }

    public Offer findOffer(int id) {

        return offerRepository.findById(id);
    }

    public void saveOffer(Offer offer) {
        offerRepository.save(offer);
    }

    public void createProductOffer(Product product, double discount, String description) throws Exception {
        if (productOfferRepository.existsByProduct(product)) {
            throw new Exception("offer is already set");
        }
        double original = getHistoryPrice(product);
        Boolean offerapplied = offerRepository.existsByCategory(product.getCategory());
        if (!offerapplied) {
            double discountedPrice = calculateDiscountedPrice(original, discount);
            product.setPrice(discountedPrice);
            productRepository.save(product);
            // double discountOffer = getDiscountOfferForProduct(product, discount);
        } else {
            Offer offer = offerRepository.findByCategory(product.getCategory());
            double discountedPrice = calculateDiscountedPrice(original, offer.getDiscount() + discount);
            product.setPrice(discountedPrice);
            productRepository.save(product);
        }
        ProductOffer prooffer = new ProductOffer();
        prooffer.setDiscountOffer(discount);
        prooffer.setLocalDateTime(LocalDateTime.now());
        prooffer.setOfferDescription(description);
        prooffer.setProduct(product);
        productOfferRepository.save(prooffer);
    }

    public void deleteProductOffer(int id) {
        ProductOffer offer = productOfferRepository.findById(id);
        Product product = offer.getProduct();
        double price = getHistoryPrice(product);
        if (offerRepository.existsByCategory(product.getCategory())) {
            Offer off = offerRepository.findByCategory(product.getCategory());
            double discountedprice = calculateDiscountedPrice(price, off.getDiscount());
            product.setPrice(discountedprice);
        } else {
            product.setPrice(price);
        }
        productRepository.save(product);
        productOfferRepository.delete(offer);

    }

    public List<ProductOffer> getAllProductOffer() {
        return productOfferRepository.findAll();
    }

    public Offer findOfferByCategory(Category category)
    {
        return offerRepository.findByCategory(category);
    }

    public ProductOffer findByProduct(Product product){
        return productOfferRepository.findByProduct(product);
    }

    public void updateProductOffer(double discount, String description, Product product) {
        ProductOffer productOffer=findByProduct(product);
        double original = getHistoryPrice(product);
        Boolean offerapplied = offerRepository.existsByCategory(product.getCategory());
        if (!offerapplied) {
            double discountedPrice = calculateDiscountedPrice(original, discount);
            product.setPrice(discountedPrice);
            productRepository.save(product);
            // double discountOffer = getDiscountOfferForProduct(product, discount);
        } else {
            Offer offer = offerRepository.findByCategory(product.getCategory());
            double discountedPrice = calculateDiscountedPrice(original, offer.getDiscount() + discount);
            product.setPrice(discountedPrice);
            productRepository.save(product);
        }
        productOffer.setDiscountOffer(discount);
        productOffer.setOfferDescription(description);
        productOffer.setProduct(product);
        productOffer.setLocalDateTime(LocalDateTime.now());
        productOfferRepository.save(productOffer);
    }   
    
    
}
