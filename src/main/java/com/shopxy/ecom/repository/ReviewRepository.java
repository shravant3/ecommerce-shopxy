package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.Review;
import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {

    public List<Review> findByProduct(Product product);
    
}
