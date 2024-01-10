package com.shopxy.ecom.service;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.UserRepository;
import com.shopxy.ecom.dto.UserDto;


import com.shopxy.ecom.model.User;

@Service
public class UserDtlsService{

   public String code="Y2KMO93";

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelmapper;


    @Autowired
    private BCryptPasswordEncoder passwordencoder;
    
  
    public User creaUser(User user) {
        user.setUsename(user.getUsename());
    
        user.setPassword(passwordencoder.encode(user.getPassword()));
        user.setToken(0);

        user.setOtpverified(false);

        if(user.getRole().equals("ROLE_ADMIN")){
         user.setRole("ROLE_ADMIN");
         }
         else if(user.getRole().equals(code)){
            user.setRole("ROLE_ADMIN");
         }
         else{
            user.setRole("ROLE_USER");
         }
        return userRepo.save(user);
        
    }

    public User creaSell(User user) {
      user.setUsename(user.getUsename());
  
      user.setPassword(passwordencoder.encode(user.getPassword()));
      user.setToken(0);
      
      user.setRole("ROLE_SELLER");
      user.setOtpverified(false);
      return userRepo.save(user);
      
  }


    public boolean checkUserMail(String email) {
       return userRepo.existsByEmail(email);
    }

    public List<User> getalluser() {
        return userRepo.findAll();
    }


    public List<User> finduser(String usename) {
       return userRepo.findByUsename(usename);
    }
    // @Override
    // public boolean checkUserphoneNumber(String phone_number) {
    //     return userRepo.existsByPhone_number(phone_number); 
    // }


    public void changelock(int id, Boolean lock) {
         User user=userRepo.findById(id);
         if(lock){
            user.setToken(0);
            userRepo.save(user);
         }
         else{
            user.setToken(1);
            userRepo.save(user);
         }
         

    }

   public int getusercount() {
      return userRepo.countuser();
   }


   public int getadmincount() {
      // TODO Auto-generated method stub
      return userRepo.countByRole("ROLE_ADMIN");
   }

   public int getsellercount() {
      return userRepo.countByRole("ROLE_SELLER");
   }

   public User convertUserDtoUserDtls(UserDto userDto){

      User user=new User();
      user=modelmapper.map(userDto,User.class);
      return user;
   }

   public User getUserByMail(String email){
      return userRepo.findByEmail(email);
   }

   public User getUserById(int id){
      return userRepo.findById(id);
   }


public void saveUser(User principalUser) {
   userRepo.save(principalUser);
}
    
}
