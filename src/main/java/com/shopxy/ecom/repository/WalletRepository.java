package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.User;
import com.shopxy.ecom.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer>{

    public Wallet findByUser(User user);
    
}
