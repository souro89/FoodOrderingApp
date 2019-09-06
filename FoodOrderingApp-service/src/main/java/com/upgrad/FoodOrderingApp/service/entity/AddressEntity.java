package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
@NamedQueries({
        @NamedQuery(name = "getAddresses" , query = "select a from AddressEntity a"),
        @NamedQuery(name = "getAddressesByUUID" , query = "select a from AddressEntity a where a.uuid=:uuid"),
        @NamedQuery(name = "getAddressesByCustomerUUID" , query = "select a from AddressEntity a,CustomerEntity c where c.uuid=:uuid"),
})
public class AddressEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "FLAT_BUIL_NUMBER")
    private String faltBuilNumber;

    @Column(name = "LOCALITY")
    private String locality;

    @Column(name = "CITY")
    private String city;

    @Column(name = "PINCODE")
    private String pincode;

    @ManyToOne
    @JoinColumn(name="STATE_ID")
    private StateEntity stateEntity;

    @Column(name = "ACTIVE")
    private long active;

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

    public String getFaltBuilNumber() {
        return faltBuilNumber;
    }

    public void setFaltBuilNumber(String faltBuilNumber) {
        this.faltBuilNumber = faltBuilNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getStateEntity() {
        return stateEntity;
    }

    public void setStateEntity(StateEntity stateEntity) {
        this.stateEntity = stateEntity;
    }

    public long getActive() {
        return active;
    }

    public void setActive(long active) {
        this.active = active;
    }
}
