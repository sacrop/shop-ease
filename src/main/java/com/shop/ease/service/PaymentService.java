package com.shop.ease.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.PaymentRepository;
import com.shop.ease.model.Payment;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public void savePaymentOrder(Payment myOrder){
        paymentRepository.save(myOrder);
    }
    
}
