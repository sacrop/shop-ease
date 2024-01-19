package com.shop.ease.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ease.model.Wallet;
import com.shop.ease.model.WalletHistory;

public interface WalletHistoryRepository extends JpaRepository<WalletHistory,Integer>{

    public List<WalletHistory> findByWallet(Wallet wallet);
    
    
}
