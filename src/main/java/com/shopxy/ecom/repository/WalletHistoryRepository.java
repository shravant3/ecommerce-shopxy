package com.shopxy.ecom.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopxy.ecom.model.Wallet;
import com.shopxy.ecom.model.WalletHistory;

public interface WalletHistoryRepository extends JpaRepository<WalletHistory,Integer>{

    public List<WalletHistory> findByWallet(Wallet wallet);
    
    
}
