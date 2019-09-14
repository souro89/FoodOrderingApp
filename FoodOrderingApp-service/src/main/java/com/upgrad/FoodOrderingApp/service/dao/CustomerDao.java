package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
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

    public CustomerEntity getCustomer(String contactNumber) {
        try {
            return entityManager.createNamedQuery("phoneNumber", CustomerEntity.class).setParameter("contactNumber", contactNumber)
                    .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public CustomerAuthEntity storeToken(CustomerAuthEntity customerAuthEntity){
        entityManager.persist(customerAuthEntity);
        return customerAuthEntity;
    }

    public CustomerAuthEntity getCusotmerAuth(String accessToken){
        try {
            return entityManager.createNamedQuery("customerAuth", CustomerAuthEntity.class)
                    .setParameter("accessToken", accessToken).getSingleResult();
        }catch (NoResultException exe){
            return null;
        }
    }

    public void updateCustomerAuthEntity(CustomerAuthEntity customerAuthEntity){
        entityManager.merge(customerAuthEntity);
    }

    public void updateCustomerEntity(CustomerEntity customerEntity){
        entityManager.merge(customerEntity);
    }

    /* Method to fetch customer object based on customer UUID*/
    public CustomerEntity getCustomerByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("customerByUUID", CustomerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
