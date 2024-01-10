package com.shopxy.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.shopxy.ecom.model.Product;




@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{
    
    public Product findById(long id);

    public Product findByPname(String pname);

    @Query("SELECT count(*) FROM Product")
    public int countproduct();

    public void deleteById(Long id);

    public List<Product> findByDeletedFalse();

    public List<Product> getByPname(String pname);

    @Query("SELECT pname FROM Product WHERE pname = :pname")
     public String getpname(String pname);

    
}
