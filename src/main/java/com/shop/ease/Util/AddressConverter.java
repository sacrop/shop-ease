package com.shop.ease.Util;

import com.shop.ease.model.Address;

public class AddressConverter {

    public static Address convertStringToAddress(String addresString) {
        Address address = new Address();
        return address;
    }

    public static String convertAddressToString(Address address) {
        // create formatted string that represents the address
        String formattedAddress = String.format(
                "Full Name: %s, Mobile: %s, Pin: %s, Building: %s, Street: %s, City: %s, State: %s, Country: %s",
                address.getFullName(),
                address.getMobilePhoneNumber(),
                address.getPinCode(),
                address.getBuildingNo(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry());

        return formattedAddress;
    }
}
