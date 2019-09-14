package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "PAYMENT")
@NamedQueries({
        @NamedQuery(name="allPaymentMethods",query="select p from PaymentEntity p"),
        @NamedQuery(name = "paymentByUUID", query = "select q from PaymentEntity q where q.uuid = :uuid"),
})
public class PaymentEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "PAYMENT_NAME")
    private String paymentName;

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

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
