package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PaymentDao {

    @PersistenceContext
    private EntityManager entityManager;

    /*Method to get available payment modes*/
    public List<PaymentEntity> getAllPaymentMethods() {
        try {
            return entityManager.createNamedQuery("allPaymentMethods", PaymentEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /*fetch payment entity based on payment uuid*/
    public PaymentEntity getPaymentByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("paymentByUUID", PaymentEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
