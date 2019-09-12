package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.swing.text.html.parser.Entity;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ItemEntity getItemByUUID(String uuid){
        try{
            return entityManager.createNamedQuery("getItemByUUID",ItemEntity.class)
                        .setParameter("uuid",uuid).getSingleResult();
        }catch (NoResultException exe){
            return null;
        }
    }
}
