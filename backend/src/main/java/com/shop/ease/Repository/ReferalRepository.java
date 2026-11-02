package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.MyUserDetails;
import com.shop.ease.model.Refferal;


@Repository
public interface ReferalRepository extends JpaRepository<Refferal,Integer> {

    public Refferal  findByUser(MyUserDetails user);
    
    public Refferal findByReferalCode(String referalCode);

    public boolean existsByReferalCode(String referalCode);
}
