package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Address;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long>{

    @Query("select c from Address c where c.defaultAddress=true")
    public Address getdefaultAddress();

    @Query("select c from Address c where c.id=:id")
    public Address getAddressById(@Param("id")long id);

    public List<Address> findByDeletedFalse();
    
}
