package com.shopxy.ecom.model;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;
@Entity
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedProductFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedProductFilter", condition = "deleted = :isDeleted")
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String pname;
    
    private String pdescription;
    
    private List<String> imagepath;

    // @Min(0)
    private double price;

    @ManyToOne
    @JoinColumn(name = "cat_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int stockQuantity;

    private boolean deleted = Boolean.FALSE;


    
}
