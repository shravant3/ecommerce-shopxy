package com.shopxy.ecom.dto;


import java.util.List;

import lombok.Data;

@Data
public class ProductDto {
    private long id;
    private String pname;
    private String pdescription;
    private List<String> imagepath;
    private double price;
    private int categoryId;
    private int userid;
    private int stockQuantity;
    private boolean deleted;
}
