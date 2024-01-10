package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.CartItem;
import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.User;

import java.util.List;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    
    public boolean existsByProduct(Product product);

    public List<CartItem> findByUserDtls(User userDtls);

    public CartItem findByUserDtlsAndProduct(User user, Product product);

    public CartItem findById(long id);

    

    
}
