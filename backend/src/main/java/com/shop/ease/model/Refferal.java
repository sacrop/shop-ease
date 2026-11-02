package com.shop.ease.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;


@Entity
public class Refferal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String referalCode;

    @OneToOne
    private MyUserDetails user;


    private int referalCount=0;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getReferalCode() {
        return referalCode;
    }


    public void setReferalCode(String referalCode) {
        this.referalCode = referalCode;
    }


    public MyUserDetails getUser() {
        return user;
    }


    public void setUser(MyUserDetails user) {
        this.user = user;
    }


    public int getReferalCount() {
        return referalCount;
    }


    public void setReferalCount(int referalCount) {
        this.referalCount = referalCount;
    }

    

    
    
    
}
