package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Refferal;
import com.shop.ease.model.UserDtls;




@Repository
public interface ReferalRepository extends JpaRepository<Refferal,Integer> {

    public Refferal  findByUser(UserDtls user);
    
    public Refferal findByReferalCode(String referalCode);

    public boolean existsByReferalCode(String referalCode);
}
