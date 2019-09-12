package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrdersDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrdersDao ordersDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderEntity> getOrdersbyCustomer(CustomerEntity customerEntity){

        List<OrderEntity> orderEntities = ordersDao.getOrdersByCustomer(customerEntity);

        Collections.sort(orderEntities, new Comparator<OrderEntity>() {
            @Override
            public int compare(OrderEntity o1, OrderEntity o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return orderEntities;
    }

}
