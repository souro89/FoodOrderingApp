package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ORDERS")
@NamedQueries({
        @NamedQuery(name = "ordersByAddress", query = "select q from OrderEntity q where q.addressEntity = :addressEntity"),
        @NamedQuery(name = "ordersByCustomer", query = "select q from OrderEntity q where q.customerEntity = :customerEntity order by q.date desc "),
        @NamedQuery(name = "ordersByRestaurant", query = "select q from OrderEntity q where q.restaurant = :restaurant"),
})
public class OrderEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "BILL")
    private BigDecimal bill;

    @ManyToOne
    @JoinColumn(name = "COUPON_ID")
    private CouponEntity couponEntity;

    @Column(name = "DISCOUNT")
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "PAYMENT_ID")
    private PaymentEntity paymentEntity;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private CustomerEntity customerEntity;

    @ManyToOne
    @JoinColumn(name = "ADDRESS_ID")
    private AddressEntity addressEntity;

    @ManyToOne
    @JoinColumn(name = "RESTAURANT_ID")
    @NotNull
    private RestaurantEntity restaurant;




    @Column(name = "date")
    @NotNull
    private Date date;

    public OrderEntity() {}

    public OrderEntity( String uuid, Double bill,
                       CouponEntity coupon, Double discount,
                        Date date,PaymentEntity paymentEntity,
                        CustomerEntity customerEntity,AddressEntity addressEntity, RestaurantEntity restaurant) {
        this.uuid = uuid;
        this.bill = new BigDecimal(bill);
        this.couponEntity = coupon;
        this.discount = new BigDecimal(discount);
        this.date = date;
        this.paymentEntity = paymentEntity;
        this.customerEntity = customerEntity;
        this.addressEntity = addressEntity;
        this.restaurant = restaurant;
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public CouponEntity getCouponEntity() {
        return couponEntity;
    }

    public void setCouponEntity(CouponEntity couponEntity) {
        this.couponEntity = couponEntity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public PaymentEntity getPaymentEntity() {
        return paymentEntity;
    }

    public void setPaymentEntity(PaymentEntity paymentEntity) {
        this.paymentEntity = paymentEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public AddressEntity getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant= restaurant;
    }
}
