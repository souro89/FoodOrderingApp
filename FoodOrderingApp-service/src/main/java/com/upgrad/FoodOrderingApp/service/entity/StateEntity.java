package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "STATE")
@NamedQueries({
        @NamedQuery(name = "getStates" , query = "select s from StateEntity s"),
        @NamedQuery(name = "getStateByUUID" , query = "select s from StateEntity s where s.uuid=:uuid")
})
public class StateEntity {


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "STATE_NAME")
    private String stateName;

}
