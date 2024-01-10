package com.shopxy.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.PaymentRepository;
import com.shopxy.ecom.model.Payment;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public void savePaymentOrder(Payment myOrder){
        paymentRepository.save(myOrder);
    }
    
}
