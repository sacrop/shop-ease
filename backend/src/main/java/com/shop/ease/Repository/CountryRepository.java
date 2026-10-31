package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country,Integer>{
    public Country findByCountryName(String countryName);
}
