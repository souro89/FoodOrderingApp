package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerEntity saveCustomer(CustomerEntity customerEntity){
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    public boolean checkContactNumber(String contactNumber){

        try {
            entityManager.createNamedQuery("phoneNumber", CustomerEntity.class).setParameter("contactNumber", contactNumber)
                    .getSingleResult();
            return true;
        }catch(NoResultException nre){
            return false;
        }

    }

}
