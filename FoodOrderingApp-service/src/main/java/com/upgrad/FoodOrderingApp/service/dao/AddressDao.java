package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AddressEntity saveAddress(AddressEntity addressEntity){
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    public List<AddressEntity> getAddresses(){
        try {
            return entityManager.createNamedQuery("getAddresses", AddressEntity.class).getResultList();
        }catch (NoResultException exe){
            return null;
        }
    }

    public AddressEntity getAddressesByUUID(String uuid){
        try {
            return entityManager.createNamedQuery("getAddressesByUUID", AddressEntity.class).setParameter("uuid", uuid)
                    .getSingleResult();
        }catch (NoResultException exe){
            return null;
        }
    }

    public AddressEntity deleteAddress(AddressEntity addressEntity){
        entityManager.remove(addressEntity);
        return addressEntity;
    }

    public List<StateEntity> getStates(){
        try {
            return entityManager.createNamedQuery("getStates", StateEntity.class).getResultList();
        }catch (NoResultException exe){
            return null;
        }
    }

    public StateEntity getStateByUUID(String uuid){
        try {
            return entityManager.createNamedQuery("getStateByUUID", StateEntity.class).setParameter("uuid", uuid)
                    .getSingleResult();
        }catch (NoResultException exe){
            return null;
        }
    }

}
