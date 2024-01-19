package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Payment;
@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>{
    
}
