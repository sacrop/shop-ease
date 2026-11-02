package com.shop.ease.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.UserRepository;
import com.shop.ease.configuration.CustomUserDetails;
import com.shop.ease.model.MyUserDetails;


@Service
public class UserDtlsService implements UserDetailsService {

    @Autowired
    private UserRepository userrepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        MyUserDetails user=userrepo.findByEmail(email);

        if(user!=null){
            return new CustomUserDetails(user);
        }
        else{
            throw new UsernameNotFoundException("username not found");
        }
    }
    
}
