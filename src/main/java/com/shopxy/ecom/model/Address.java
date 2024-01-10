package com.shopxy.ecom.model;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@SQLDelete(sql = "UPDATE addresses SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedAddressFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedAddressFilter", condition = "deleted = :isDeleted")
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String fullName;

    @Pattern(regexp="\\d{10}",message = "mobile phone number must be exactly 10 digits")
    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    private String pinCode;
    private String buildingNo;

    private String street;
    private String city;
    private String Landmark;
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "is_default")
    private Boolean defaultAddress;

    private boolean deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userDtls;

    @Override
    public String toString() {
        return "Address [id=" + id + ", fullName=" + fullName + ", mobilePhoneNumber=" + mobilePhoneNumber
                + ", pinCode=" + pinCode + ", buildingNo=" + buildingNo + ", street=" + street + ", city=" + city
                + ", Landmark=" + Landmark + ", state=" + state + ", country=" + country + ", defaultAddress="
                + defaultAddress + ", deleted=" + deleted + "]";
    }

    

}
