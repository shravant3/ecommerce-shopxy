package com.shopxy.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.shopxy.ecom.model.Country;
import com.shopxy.ecom.repository.CountryRepository;

@Service
public class CountryService {
    @Autowired
    CountryRepository countrepo;

    public List<Country> getallCountries()
    {
        return countrepo.findAllCountries();
    }

    public Country getcountryByid(int id) {
        return countrepo.findByid(id);
    }
    
}
