package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.swing.text.html.parser.Entity;
import java.util.List;

@Repository
public class OrdersDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<OrderEntity> getOrdersByRestaurant(RestaurantEntity restaurant){
        try{
            return entityManager.createNamedQuery("ordersByRestaurant",OrderEntity.class)
                    .setParameter("restaurant",restaurant)
                    .getResultList();
        }catch(NoResultException exe){
            return null;
        }
    }

    public List<OrderEntity> getOrdersByCustomer(CustomerEntity customerEntity){
        try{
            return entityManager.createNamedQuery("ordersByCustomer",OrderEntity.class)
                    .setParameter("customerEntity",customerEntity)
                    .getResultList();
        }catch(NoResultException exe){
            return null;
        }
    }

    /* Method to create new order */
    public OrderEntity createOrder(OrderEntity orderEntity) {
        entityManager.persist(orderEntity);
        return orderEntity;
    }

}
