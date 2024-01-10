package com.shopxy.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.ProductPriceHistoryRepository;
import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.ProductPriceHistory;

@Service
public class ProductPriceHistoryService {
    
    @Autowired
    private ProductPriceHistoryRepository priceHistoryRepository;

    // @Autowired 
    // priv

    public ProductPriceHistory  getOriginalPriceOfProduct(Product product ){
        return priceHistoryRepository.findByProduct(product);
    }
}
