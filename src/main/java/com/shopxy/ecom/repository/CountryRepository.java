package com.shopxy.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Country;
@Repository
public interface CountryRepository extends JpaRepository<Country,Integer>{
    @Query("SELECT C FROM Country C")
  public List<Country> findAllCountries();

  @Query("SELECT c.countryName from Country c")
  public List<String> getAllCountryName();

  public Country findByid(int id);

}
