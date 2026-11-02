package com.shop.ease.configuration;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shop.ease.model.MyUserDetails;




public class CustomUserDetails implements UserDetails {

    @Autowired
    private MyUserDetails myUserDetails;

    public CustomUserDetails(MyUserDetails myUserDetails) {
        this.myUserDetails = myUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(myUserDetails.getRole());
        return Arrays.asList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
       
        return myUserDetails.getPassword();
    }

    @Override
    public String getUsername() {
        
        return myUserDetails.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if(myUserDetails.getToken()==0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean isCredentialsNonExpired() {
        
        return true;
    }

    @Override
    public boolean isEnabled() {
        if(myUserDetails.isOtpverified()==true){
            return true;
           } 
           else{
                return false;
           }
    }
    
}
