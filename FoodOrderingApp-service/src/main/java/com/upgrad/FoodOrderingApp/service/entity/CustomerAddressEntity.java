package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER_ADDRESS")
public class CustomerAddressEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "CUSTOMER_ID")
    private long customerEntity;


    @Column(name = "ADDRESS_ID")
    private long addressEntity;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(long customerEntity) {
        this.customerEntity = customerEntity;
    }

    public long getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(long addressEntity) {
        this.addressEntity = addressEntity;
    }

    public void setAddressEntity(int addressEntity) {
        this.addressEntity = addressEntity;
    }
}
