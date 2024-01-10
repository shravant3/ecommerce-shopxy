package com.shopxy.ecom.service;


import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.CategoryRepository;
import com.shopxy.ecom.dto.CategoryDto;

import com.shopxy.ecom.model.Category;



import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository catrepo;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;


    public void createcat(Category cat) {
        catrepo.save(cat);
    }

    public List<Category> getallcategory()
    {
        return catrepo.findAllCategory();
    }

    public Boolean existsByCatname(String catname) {
    
            return catrepo.existsByCatname(catname);
        
    }  

    public List<Category> getdeletedCategories(Boolean isDeleted){
        Session session=entityManager.unwrap(Session.class);
        Filter filter=session.enableFilter("deletedCategoryFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<Category> cat=catrepo.findAll();
        session.disableFilter("deletedCategoryFilter");
        return cat ;
    }

    public List<Category> getNotdeletedCategories(){
        return catrepo.findByDeletedFalse();
    }
    

    public Category getCategorybyname(String name){
        return catrepo.findByCatname(name);

    }


    public Category convertCategoryDtotCategory(CategoryDto categoryDto)
    {
        Category category=new Category();
     category=modelMapper.map(categoryDto,Category.class);
     return category;
    }


    public List<String> getAllCategoryNames()
    {
        return catrepo.getAllCategoryName();
    }

    //return category based on category id
    public Category getcategoryByid(int catid) {
        return catrepo.findByCatid(catid);
    }

    @Transactional
    public void deleteCategoryByid(int id) { 
        // subcatrepo.deleteByCategory(catrepo.findByCatid(id));
        catrepo.deleteById(id);
    }

    public void save(Category category) {
        catrepo.save(category);
    }
}
