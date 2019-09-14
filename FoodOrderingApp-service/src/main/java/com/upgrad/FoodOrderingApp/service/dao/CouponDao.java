package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CouponDao {


    @PersistenceContext
    private EntityManager entityManager;

    public CouponEntity getCouponByName(String couponName){
        try{
            return entityManager.createNamedQuery("getCouponByName",CouponEntity.class)
                    .setParameter("couponName",couponName)
                    .getSingleResult();
        }catch (NoResultException exe){
            return null;
        }
    }

    /* fetch  coupon entity for a coupon uuid */
    public CouponEntity getCouponByCouponUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("couponByUUID", CouponEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
