package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Refferal;
import com.shopxy.ecom.model.User;




@Repository
public interface ReferalRepository extends JpaRepository<Refferal,Integer> {

    public Refferal  findByUser(User user);
    
    public Refferal findByReferalCode(String referalCode);

    public boolean existsByReferalCode(String referalCode);
}
