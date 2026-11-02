package com.shop.ease.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "wallet")
public class Wallet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUserDetails user;

    private Double currentBalance=0.0;

    @OneToMany(mappedBy ="wallet",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WalletHistory> WalletHistory;

    public Wallet() {
        this.currentBalance = 0.0;
    }

    @Override
    public String toString() {
        return "Wallet [id=" + id + ", user=" + user + ", currentBalance=" + currentBalance + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MyUserDetails getUser() {
        return user;
    }

    public void setUser(MyUserDetails user) {
        this.user = user;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public List<WalletHistory> getWalletHistory() {
        return WalletHistory;
    }

    public void setWalletHistory(List<WalletHistory> walletHistory) {
        WalletHistory = walletHistory;
    }

    
  
}
