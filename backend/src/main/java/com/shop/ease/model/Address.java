package com.shop.ease.model;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@SQLDelete(sql = "UPDATE addresses SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedAddressFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedAddressFilter", condition = "deleted = :isDeleted")
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String fullName;

    @Pattern(regexp="\\d{10}",message = "mobile phone number must be exactly 10 digits")
    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    private String pinCode;
    private String buildingNumber;

    private String street;
    private String city;
    private String Landmark;
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "is_default")
    private Boolean defaultAddress;

    private boolean deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MyUserDetails userDtls;

    @Override
    public String toString() {
        return "Address [id=" + id + ", fullName=" + fullName + ", mobilePhoneNumber=" + mobilePhoneNumber
                + ", pinCode=" + pinCode + ", buildingNumber=" + buildingNumber + ", street=" + street + ", city=" + city
                + ", Landmark=" + Landmark + ", state=" + state + ", country=" + country + ", defaultAddress="
                + defaultAddress + ", deleted=" + deleted + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Boolean getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public MyUserDetails getUserDtls() {
        return userDtls;
    }

    public void setUserDtls(MyUserDetails userDtls) {
        this.userDtls = userDtls;
    }

    

    

}
