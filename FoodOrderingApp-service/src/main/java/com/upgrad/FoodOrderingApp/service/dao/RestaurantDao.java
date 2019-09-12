package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantEntity> getAllRestaurants(){
        try{
            return entityManager.createNamedQuery("getAllRestaurants",RestaurantEntity.class)
                        .getResultList();
        }catch(NoResultException nre){
            return null;
        }
    }

    public List<RestaurantEntity> restaurantsByName(String name){
        try{
            return entityManager.createNamedQuery("getRestaurantsByName",RestaurantEntity.class)
                    .setParameter("restaurantName",name.toUpperCase())
                    .getResultList();
        }catch(NoResultException nre){
            return null;
        }
    }

    public RestaurantEntity restaurantByUUID(String uuid){
        try{
            return entityManager.createNamedQuery("getRestaurantByUUID",RestaurantEntity.class)
                    .setParameter("uuid",uuid).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public void updateRestaurant(RestaurantEntity restaurantEntity){
        entityManager.merge(restaurantEntity);
    }



}
