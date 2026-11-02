package com.shop.ease.model;
import java.time.LocalDateTime;

import com.shop.ease.Util.WalletTransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallet_transaction")
public class WalletHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int walletHistoryId;
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private LocalDateTime transactionTime;

    private double amountTransferred;

    private String description;

    @Column(name="transaction status")
    @Enumerated(EnumType.STRING)
    private WalletTransactionStatus status;

    public int getWalletHistoryId() {
        return walletHistoryId;
    }

    public void setWalletHistoryId(int walletHistoryId) {
        this.walletHistoryId = walletHistoryId;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public double getAmountTransferred() {
        return amountTransferred;
    }

    public void setAmountTransferred(double amountTransferred) {
        this.amountTransferred = amountTransferred;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WalletTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(WalletTransactionStatus status) {
        this.status = status;
    }

    
}
