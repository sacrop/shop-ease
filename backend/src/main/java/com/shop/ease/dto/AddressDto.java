package com.shop.ease.dto;

import com.shop.ease.model.UserDtls;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Data
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
    private UserDtls userDtls;
    public AddressDto(String fullName, String mobilePhoneNumber, String pinCode, String buildingNo, String street,
            String city, String landmark, String state, Boolean defaultAddress, UserDtls userDtls) {
        this.fullName = fullName;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.pinCode = pinCode;
        this.buildingNo = buildingNo;
        this.street = street;
        this.city = city;
        this.landmark = landmark;
        this.state = state;
        this.defaultAddress = defaultAddress;
        this.userDtls = userDtls;
    }
}
