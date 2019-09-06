package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "RESTAURANT_ITEM")
public class RestaurantItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ITEM_ID")
    private int itemEntity;

    @Column(name = "RESTAURANT_ID")
    private int restaurantEntity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(int itemEntity) {
        this.itemEntity = itemEntity;
    }

    public int getRestaurantEntity() {
        return restaurantEntity;
    }

    public void setRestaurantEntity(int restaurantEntity) {
        this.restaurantEntity = restaurantEntity;
    }
}
