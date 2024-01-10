package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Category;
import com.shopxy.ecom.model.Offer;


@Repository
public interface OfferRepository extends JpaRepository<Offer,Integer> {

    
    public Offer findById(int id);
    
    public boolean existsByCategory(Category category);
     
}
