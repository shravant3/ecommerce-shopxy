package com.shopxy.ecom.repository;

 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.User;



@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    // public boolean existsByPhone_number(String phone_number);
    public boolean existsByEmail(String email);

    public User findByEmail(String email);

    public List<User> findByUsename(String usename);

    public User findById(int id);

    public User findByPhonenumber(String phonenumber);

    @Query("SELECT COUNT(*) FROM User")
    public int countuser();

    public int countByRole(String role);
     
}
