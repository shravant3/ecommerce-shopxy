package com.shopxy.ecom.dto;

import com.shopxy.ecom.model.Country;
import com.shopxy.ecom.model.User;

import lombok.Data;

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
    private Country country;
    private Boolean defaultAddress;
    private User userDtls;
    public AddressDto(String fullName, String mobilePhoneNumber, String pinCode, String buildingNo, String street,
            String city, String landmark, String state, Country country, Boolean defaultAddress, User userDtls) {
        this.fullName = fullName;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.pinCode = pinCode;
        this.buildingNo = buildingNo;
        this.street = street;
        this.city = city;
        this.landmark = landmark;
        this.state = state;
        this.country = country;
        this.defaultAddress = defaultAddress;
        this.userDtls = userDtls;
    }
    
}
