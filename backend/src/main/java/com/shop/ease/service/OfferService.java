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

    private final OfferRepository offerRepository;
    private final ProductPriceHistoryRepository priceHistoryRepository;
    private final ProductRepository productRepository;
    private final ProductOfferRepository productOfferRepository;

    public OfferService(
        OfferRepository offerRepository,
        ProductPriceHistoryRepository priceHistoryRepository,
        ProductRepository productRepository,
        ProductOfferRepository productOfferRepository
    ) {
        this.offerRepository = offerRepository;
        this.priceHistoryRepository = priceHistoryRepository;
        this.productRepository = productRepository;
        this.productOfferRepository = productOfferRepository;
    }

    @Transactional
    public void createCategoryOffer(double discount, Category category, String description) {
        for (Product product : category.getProduct()) {
            double original = getHistoryPrice(product);
            double discountOffer = getDiscountOfferForProduct(product, discount);
            double discountedPrice = calculateDiscountedPrice(original, discountOffer);
            product.setProductPrice(discountedPrice);
            productRepository.save(product);
        }
        Offer offer = new Offer();
        offer.setCategory(category);
        offer.setOfferDiscount(discount);
        offer.setOfferDescription(description);
        offerRepository.save(offer);
    }

    private double getHistoryPrice(Product product) {
        if (priceHistoryRepository.existsByProduct(product)) {
            ProductPriceHistory priceHistory = priceHistoryRepository.findByProduct(product);
            return priceHistory.getProductOriginalPrice();

        } else {
            ProductPriceHistory priceHistory = new ProductPriceHistory();
            priceHistory.setProduct(product);
            priceHistory.setChangeDate(LocalDateTime.now());
            priceHistory.setProductOriginalPrice(product.getProductPrice());
            priceHistoryRepository.save(priceHistory);
            return priceHistory.getProductOriginalPrice();
        }

    }

    private double calculateDiscountedPrice(double original, double discountOffer) {
        double rate = (original * discountOffer) / 100;
        return original - rate;
    }

    private double getDiscountOfferForProduct(Product product, double discount) {
        if (productOfferRepository.existsByProduct(product)) {
            ProductOffer productOffer = productOfferRepository.findByProduct(product);
            return productOffer.getProductOfferDiscount() + discount;
        } else {
            return discount;
        }
    }

    public List<Offer> getAllOffer() {
        return offerRepository.findAll();
    }

    public void deleteOffer(int id) {
        Offer offer = offerRepository.findByOfferId(id);
        Category category = offer.getCategory();
        for (Product pro : category.getProduct()) {
            if (offer.getOfferDiscount() == getDiscountOfferForProduct(pro, offer.getOfferDiscount())) {
                pro.setProductPrice(getHistoryPrice(pro));
            } else {
                double balanceDiscount = getDiscountOfferForProduct(pro, offer.getOfferDiscount()) - offer.getOfferDiscount();
                double price = calculateDiscountedPrice(getHistoryPrice(pro), balanceDiscount);
                pro.setProductPrice(price);
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

        return offerRepository.findByOfferId(id);
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
            product.setProductPrice(discountedPrice);
            productRepository.save(product);
            // double discountOffer = getDiscountOfferForProduct(product, discount);
        } else {
            Offer offer = offerRepository.findByCategory(product.getCategory());
            double discountedPrice = calculateDiscountedPrice(original, offer.getOfferDiscount() + discount);
            product.setProductPrice(discountedPrice);
            productRepository.save(product);
        }
        ProductOffer prooffer = new ProductOffer();
        prooffer.setProductOfferDiscount(discount);
        prooffer.setLocalDateTime(LocalDateTime.now());
        prooffer.setProductOfferDescription(description);
        prooffer.setProduct(product);
        productOfferRepository.save(prooffer);
    }

    public void deleteProductOffer(int id) {
        ProductOffer offer = productOfferRepository.findByProductOfferId(id);
        Product product = offer.getProduct();
        double price = getHistoryPrice(product);
        if (offerRepository.existsByCategory(product.getCategory())) {
            Offer off = offerRepository.findByCategory(product.getCategory());
            double discountedprice = calculateDiscountedPrice(price, off.getOfferDiscount());
            product.setProductPrice(discountedprice);
        } else {
            product.setProductPrice(price);
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
            product.setProductPrice(discountedPrice);
            productRepository.save(product);
            // double discountOffer = getDiscountOfferForProduct(product, discount);
        } else {
            Offer offer = offerRepository.findByCategory(product.getCategory());
            double discountedPrice = calculateDiscountedPrice(original, offer.getOfferDiscount() + discount);
            product.setProductPrice(discountedPrice);
            productRepository.save(product);
        }
        productOffer.setProductOfferDiscount(discount);
        productOffer.setProductOfferDescription(description);
        productOffer.setProduct(product);
        productOffer.setLocalDateTime(LocalDateTime.now());
        productOfferRepository.save(productOffer);
    }   
    
    
}
