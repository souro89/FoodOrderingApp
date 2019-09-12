package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CategoryEntity categoryByUUID(String uuid){
        try{
            return entityManager.createNamedQuery("getCategoryByUUID",CategoryEntity.class)
                    .setParameter("uuid",uuid).getSingleResult();
        }catch(NoResultException exe){
            return null;
        }
    }

    public List<CategoryEntity> getCategories(){
        try{
            return entityManager.createNamedQuery("getCategories",CategoryEntity.class)
                    .getResultList();
        }catch(NoResultException exe){
            return null;
        }
    }
}
