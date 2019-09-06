package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RESTAURANT")
public class RestaurantEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "RESTAURANT_NAME")
    private String restaurantName;

    @Column(name = "PHOTO_URL")
    private String photoUrl;

    @Column(name = "CUSTOMER_RATING")
    private int customerRating;

    @Column(name = "AVERAGE_PRICE_FOR_TWO")
    private int averagePriceForTwo;

    @Column(name = "NUMBER_OF_CUSTOMERS_RATED")
    private int numberOfCustomersRated;

    @ManyToOne
    @JoinColumn(name = "ADDRESS_ID")
    private AddressEntity addressEntity;


    @ManyToMany
    @JoinTable(name = "restaurant_category", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<CategoryEntity> categories = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "restaurant_item", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemEntity> items = new ArrayList<>();

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(int customerRating) {
        this.customerRating = customerRating;
    }

    public int getAveragePriceForTwo() {
        return averagePriceForTwo;
    }

    public void setAveragePriceForTwo(int averagePriceForTwo) {
        this.averagePriceForTwo = averagePriceForTwo;
    }

    public int getNumberOfCustomersRated() {
        return numberOfCustomersRated;
    }

    public void setNumberOfCustomersRated(int numberOfCustomersRated) {
        this.numberOfCustomersRated = numberOfCustomersRated;
    }

    public AddressEntity getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }
}
