package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Payment;
@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>{
    
}
