package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopxy.ecom.model.User;
import com.shopxy.ecom.model.Wishlist;



public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    public Wishlist findByUser(User user);
}
