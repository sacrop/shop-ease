package com.shop.ease.service;

import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.AddressRepository;
import com.shop.ease.Repository.CountryRepository;
import com.shop.ease.dto.AddressDto;
import com.shop.ease.model.Address;
import com.shop.ease.model.Country;
import com.shop.ease.model.UserDtls;

import jakarta.persistence.EntityManager;


@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityManager entityManager;

    public List<Country> getAllCountry() {
        return countryRepository.findAll();
    }

    public Address addressDtoToAddress(AddressDto addressdto) {
        Address address=new Address();
        address=modelMapper.map(addressdto,Address.class);
        return address;
    }


    public void saveAddress(Address address)
    {
        addressRepository.save(address);

    }

    public List<Address> getAlladdress() {
        return addressRepository.findAll();
    }

    public Address getAddressById(Long id) {
        return addressRepository.getAddressById(id); 
    }

    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    public List<Address> getDeletedAddress() {
        Boolean isDeleted=false;
        Session session=entityManager.unwrap(Session.class);
        Filter filter=session.enableFilter("deletedAddressFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<Address> ladd=addressRepository.findAll();
        session.disableFilter("deletedAddressFilter");
        return ladd;
    }

    public List<Address> getNonDeletedAddress(){
        return addressRepository.findByDeletedFalse();
    }

    public void updateAddress(Address address, String countryname) {
        Country country=countryRepository.findByCountryName(countryname);
        address.setCountry(country);
        if(address.getDefaultAddress()==true){
            uncheckDefaultAddress(address.getUserDtls());
        }
        addressRepository.save(address);
    }
    
    public  void uncheckDefaultAddress(UserDtls user) {

        for(Address address:addressRepository.findByUserDtls(user)){
            if(address.getDefaultAddress()){
                address.setDefaultAddress(false);
                addressRepository.save(address);
            }
        }
    }


}
