package com.shopxy.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

  public boolean existsByCatname(String name);

  public Category findByCatname(String catname);

  @Query("SELECT C FROM Category C")
  public List<Category> findAllCategory();

  @Query("SELECT c.catname from Category c")
  public List<String> getAllCategoryName();

  public Category findByCatid(int catid);

  public boolean existsByCatnameAndDeleted(String catname, boolean deleted);

  public List<Category> findByDeletedFalse();

  @Query("SELECT catname FROM Category WHERE catname = :catname")
     public String getcatname(String catname);

}
