package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "ORDER_ITEM")
public class OrderItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "ORDER_ID")
    private int orderEntity;

    @JoinColumn(name = "ITEM_ID")
    private int itemEntity;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "QUATITY")
    private int price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(int orderEntity) {
        this.orderEntity = orderEntity;
    }

    public int getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(int itemEntity) {
        this.itemEntity = itemEntity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
