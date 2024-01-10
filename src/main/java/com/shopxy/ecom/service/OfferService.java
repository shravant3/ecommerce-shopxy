package com.shopxy.ecom.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.OfferRepository;
import com.shopxy.ecom.repository.ProductPriceHistoryRepository;
import com.shopxy.ecom.repository.ProductRepository;
import com.shopxy.ecom.model.Category;
import com.shopxy.ecom.model.Offer;
import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.ProductPriceHistory;

@Service
public class OfferService {
    
    @Autowired
    private OfferRepository offerRepository;
     
    @Autowired
    private ProductPriceHistoryRepository priceHistoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public void CreateCategoryOffer(double discount,Category category){
            Offer offer=new Offer();
            
            List<Product> productlist=category.getProduct();
            for(Product pro:productlist){
                ProductPriceHistory price=priceHistoryRepository.findByProduct(pro);
            if(price==null){
                price=new ProductPriceHistory();
            }
                price.setProduct(pro);
                price.setChangeDate(LocalDateTime.now());
                price.setOriginalPrice(pro.getPrice());
                priceHistoryRepository.save(price);
                double rate=(pro.getPrice()*discount)/100;
                pro.setPrice(rate);
                productRepository.save(pro);
            }
            offer.setCategory(category);
            offer.setDiscount(String.valueOf(discount));
            offerRepository.save(offer);            
    }

    public List<Offer> getAllOffer() {
        return offerRepository.findAll();
    }

    public void deleteOffer(int id) {
        Offer offer=offerRepository.findById(id);
        Category category=offer.getCategory();
        List<Product> productlist=category.getProduct();
        
        for(Product pro:productlist){
            ProductPriceHistory price=priceHistoryRepository.findByProduct(pro);
            if(price!=null){
                pro.setPrice(price.getOriginalPrice());
                priceHistoryRepository.delete(price);

            }
                productRepository.save(pro);
            }
            offerRepository.delete(offer);
    }

    public boolean findExistOffer(Category category){
        return offerRepository.existsByCategory(category);
    }
}
