package com.shop.ease.dto;

import com.shop.ease.model.MyUserDetails;


public class AddressDto {

    private long id;
    private String fullName;
    private String mobilePhoneNumber;
    private String pinCode;
    private String buildingNo;
    private String street;
    private String city;
    private String landmark;
    private String state;
    private Boolean defaultAddress;
    private MyUserDetails userDtls;
    
    private AddressDto(Builder builder) {
        this.id = builder.id;
        this.fullName = builder.fullName;
        this.mobilePhoneNumber = builder.mobilePhoneNumber;
        this.pinCode = builder.pinCode;
        this.buildingNo = builder.buildingNo;
        this.street = builder.street;
        this.city = builder.city;
        this.landmark = builder.landmark;
        this.state = builder.state;
        this.defaultAddress = builder.defaultAddress;
        this.userDtls = builder.userDtls;
    }

    public static class Builder {
        private long id;
        private String fullName;
        private String mobilePhoneNumber;
        private String pinCode;
        private String buildingNo;
        private String street;
        private String city;
        private String landmark;
        private String state;
        private Boolean defaultAddress;
        private MyUserDetails userDtls;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder setMobilePhoneNumber(String mobilePhoneNumber) {
            this.mobilePhoneNumber = mobilePhoneNumber;
            return this;
        }

        public Builder setPinCode(String pinCode) {
            this.pinCode = pinCode;
            return this;
        }

        public Builder setBuildingNo(String buildingNo) {
            this.buildingNo = buildingNo;
            return this;
        }

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setLandmark(String landmark) {
            this.landmark = landmark;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setDefaultAddress(Boolean defaultAddress) {
            this.defaultAddress = defaultAddress;
            return this;
        }

        public Builder setUserDtls(MyUserDetails userDtls) {
            this.userDtls = userDtls;
            return this;
        }

    }
}
