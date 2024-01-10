package com.shopxy.ecom.dto;

import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String usename;
    private String email;
    private String phonenumber;
    private String password;
    private String role;
    private int token;
    private boolean otpverified;
    private String gstin;
}
