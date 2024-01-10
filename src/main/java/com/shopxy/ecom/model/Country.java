package com.shopxy.ecom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Country(String countryCode, String countryName) {
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public Country() {
    }

    private String countryCode;

    private String countryName;

    @Override
    public String toString() {
        return "Country{" +
               "id=" + id +
               ", countryCode='" + countryCode + '\'' +
               ", countryName='" + countryName + '\'' +
               '}';
    }
}
