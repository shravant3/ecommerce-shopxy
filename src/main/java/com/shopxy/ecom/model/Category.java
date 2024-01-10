package com.shopxy.ecom.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;

import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@SQLDelete(sql = "UPDATE product_category SET deleted = true WHERE catid = ?")
@FilterDef(name = "deletedCategoryFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedCategoryFilter", condition = "deleted = :isDeleted")
@Table(name = "product_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int catid;
    @Column(unique = true)
    private String catname;   
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "category")
    private List<Product> product=new ArrayList<>();


    @Override
    public String toString() {
        return "Category{" +
                "catid=" + catid +
                ", catname='" + catname + '\'' +
                '}';
    }

    private boolean deleted = Boolean.FALSE;
}
