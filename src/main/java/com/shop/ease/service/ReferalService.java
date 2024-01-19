package com.shop.ease.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.ReferalRepository;
import com.shop.ease.model.Refferal;
import com.shop.ease.model.UserDtls;

@Service
public class ReferalService {
    

    @Autowired
    private ReferalRepository referalRepository;

    
    public void CreateReferral(UserDtls user) {
        UUID uuid = UUID.randomUUID();
        String randomString = uuid.toString();

        Refferal referal = new Refferal();
        referal.setUser(user);
        referal.setReferalCode(randomString);
        referalRepository.save(referal);
    }


    public Refferal findByUser(UserDtls user) {
        
        return referalRepository.findByUser(user);
    }


    public Boolean UseRefferalByCode(String referalcode) {
        Refferal refferal=referalRepository.findByReferalCode(referalcode);
        if(refferal.getReferalCount()<2){
            refferal.setReferalCount(refferal.getReferalCount()+1);
            return true;
        }
        return false;      
    }


    public UserDtls getUserBycode(String referalcode) {
        Refferal refferal=referalRepository.findByReferalCode(referalcode);
        return refferal.getUser();
    }


    public Boolean getValidcode(String referalcode) {
        return referalRepository.existsByReferalCode(referalcode);
    }
}
