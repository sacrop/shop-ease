package com.shop.ease.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "site_user")
public class MyUserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Column(nullable = false)
    private String userName;

    @Email(message = "Invalid email address")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Mobile phone number must have exactly 10 digits")
    private String phonenumber;
    private String password;
    
    private String role;


    @OneToMany(mappedBy = "userDtls", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @Column(name = "image")
    private String imagepath;
    

    private int token;

    private boolean otpverified;


    public MyUserDetails( String userName, String email, String phonenumber, String password, String role, String imagepath) {
        this.userName = userName;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        this.role = role;
        this.imagepath = imagepath;

    }

    public MyUserDetails() {
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhonenumber() {
        return phonenumber;
    }


    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }


    public List<Address> getAddresses() {
        return addresses;
    }


    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }


    public String getImagepath() {
        return imagepath;
    }


    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }


    public int getToken() {
        return token;
    }


    public void setToken(int token) {
        this.token = token;
    }


    public boolean isOtpverified() {
        return otpverified;
    }


    public void setOtpverified(boolean otpverified) {
        this.otpverified = otpverified;
    }

    
}
