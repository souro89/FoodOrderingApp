package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.criterion.Order;

import javax.persistence.*;

@Entity
@Table(name = "ORDER_ITEM")
@NamedQueries({
        @NamedQuery(name = "itemsByOrder", query = "select q from OrderItemEntity q where q.orderEntity = :orderEntity"),
})
public class OrderItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity orderEntity;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private ItemEntity itemEntity;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "PRICE")
    private int price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public ItemEntity getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(ItemEntity itemEntity) {
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
