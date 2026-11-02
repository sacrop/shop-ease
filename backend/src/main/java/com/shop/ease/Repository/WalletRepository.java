package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.MyUserDetails;
import com.shop.ease.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer>{

    public Wallet findByUser(MyUserDetails myUserDetails);
    
}
