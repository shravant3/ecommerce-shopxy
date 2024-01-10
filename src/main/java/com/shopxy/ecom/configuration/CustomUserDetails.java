package com.shopxy.ecom.configuration;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopxy.ecom.model.User;


public class CustomUserDetails implements UserDetails {

    @Autowired
    private User userDtls;

    public CustomUserDetails(User userDtls) {
        this.userDtls = userDtls;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(userDtls.getRole());
        return Arrays.asList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
       
        return userDtls.getPassword();
    }

    @Override
    public String getUsername() {
        
        return userDtls.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if(userDtls.getToken()==0){
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
        if(userDtls.isOtpverified()==true){
            return true;
           } 
           else{
                return false;
           }
    }
    
}
