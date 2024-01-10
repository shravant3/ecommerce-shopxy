package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.ProductPriceHistory;
@Repository
public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistory,Integer> {

    public ProductPriceHistory findByProduct(Product product);
    
}
