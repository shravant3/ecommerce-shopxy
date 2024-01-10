package com.shopxy.ecom.service;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.ProductRepository;
import com.shopxy.ecom.dto.ProductDto;

import com.shopxy.ecom.model.Product;

import jakarta.persistence.EntityManager;



@Service
public class ProductService {

    @Autowired
    private ProductRepository prorepo;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    public List<Product> getallproduct()
    {
        return prorepo.findAll();
    }
    public List<Product> getNotDeletedProducts(){
        return prorepo.findByDeletedFalse();
    }

    public void createproduct(Product product)
    {

        prorepo.save(product);
    }

    public void deleteproductbyid(long id) {
       
        prorepo.deleteById(id);
    }

    public Product getproductbyid(long l)
    {
        return prorepo.findById(l);
    }

    public void saveproduct(Product product) {
        prorepo.save(product);
    }

    public Product getproductbypname(String pname) {
        return prorepo.findByPname(pname);
    }

    public int getproductcount()
    {
        return prorepo.countproduct();
    }

    public Product convertProductDtoProduct(ProductDto productDto)
    {
        Product product=new Product();
        product=modelMapper.map(productDto,Product.class);
        return product;
    }
    // @Transactional
    // public void UpdateProductCategoryToNull(Category category){       
    //     Product product=prorepo.findByCategoryId(category.getCatid());
    //     System.out.println(product);
    //     // product.setCategory(null);
    //     // prorepo.save(product);
    //     System.out.println("null is set successfull");
    // }
    public List<Product> getdeletedProduct()
    {
        Boolean isDeleted=true;
        Session session=entityManager.unwrap(Session.class);
        Filter filter=session.enableFilter("deletedProductFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<Product> pro=prorepo.findAll();
        session.disableFilter("deletedProductFilter");
        return pro;

    }
    public void restoredeleted() {
        List<Product> pro=this.getdeletedProduct();
        for(Product prod:pro){
            prod.setDeleted(false);
            prorepo.save(prod);
        }
    }

    public List<Product> getProductsSortedByPrice(List<Product> products) {
        // Sort the products by price in ascending order
        Collections.sort(products, Comparator.comparing(Product::getPrice));
        return products;
    }

    // public List<Product> getProductByName() {
    //     return null;
    // }

}
