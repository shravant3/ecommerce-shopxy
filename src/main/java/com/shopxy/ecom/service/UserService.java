package com.shopxy.ecom.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.shopxy.ecom.configuration.CustomUserDetails;
import com.shopxy.ecom.repository.UserRepository;

import com.shopxy.ecom.model.User;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userrepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        User user=userrepo.findByEmail(email);

        if(user!=null){
            return new CustomUserDetails(user);
        }
        else{
            throw new UsernameNotFoundException("username not found");
        }
    }
    
}
