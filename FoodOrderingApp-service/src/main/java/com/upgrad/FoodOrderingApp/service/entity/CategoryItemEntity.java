package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY_ITEM")
public class CategoryItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "ITEM_ID")
    private int itemEntity;

    @Column(name = "CATEGORY_ID")
    private int categoryEntity;

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

    public int getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(int categoryEntity) {
        this.categoryEntity = categoryEntity;
    }
}
