package com.shopxy.ecom.service;

import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.AddressRepository;
import com.shopxy.ecom.repository.CountryRepository;
import com.shopxy.ecom.dto.AddressDto;
import com.shopxy.ecom.model.Address;
import com.shopxy.ecom.model.Country;

import jakarta.persistence.EntityManager;


@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityManager entityManager;

    public List<Country> getAllCountry() {
        return countryRepository.findAll();
    }

    public Address addressDtoToAddress(AddressDto addressdto) {
        Address address=new Address();
        address=modelMapper.map(addressdto,Address.class);
        return address;
    }

    public Address getDefaultAddress() {
        
        return addressRepository.getdefaultAddress();
    }

    public void saveAddress(Address address)
    {
        addressRepository.save(address);

    }

    public List<Address> getAlladdress() {
        return addressRepository.findAll();
    }

    public Address getAddressById(Long id) {
        return addressRepository.getAddressById(id); 
    }

    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    public List<Address> getDeletedAddress() {
        Boolean isDeleted=false;
        Session session=entityManager.unwrap(Session.class);
        Filter filter=session.enableFilter("deletedAddressFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<Address> ladd=addressRepository.findAll();
        session.disableFilter("deletedAddressFilter");
        return ladd;
    }

    public List<Address> getNonDeletedAddress(){
        return addressRepository.findByDeletedFalse();
    }


}
