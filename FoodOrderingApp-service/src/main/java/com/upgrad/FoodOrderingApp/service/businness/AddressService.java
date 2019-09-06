package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AddressService {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(String authorization,AddressEntity addressEntity) throws AuthorizationFailedException, SaveAddressException {
        String[] bearerToken = authorization.split("Bearer ");
        CustomerAuthEntity customerAuthEntity = customerDao.getCusotmerAuth(bearerToken[1]);
        if(customerAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime now = ZonedDateTime.now();
        if(now.isAfter(customerAuthEntity.getExpiresAt())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        }

        if(addressEntity.getFaltBuilNumber() == null || addressEntity.getFaltBuilNumber() == "" ||
           addressEntity.getCity() == null || addressEntity.getCity() == "" ||
           addressEntity.getLocality() == null || addressEntity.getLocality() == "" ||
           addressEntity.getPincode() == null || addressEntity.getPincode() == "" ||
           addressEntity.getStateEntity() == null
        ){
            throw new SaveAddressException("SAR-001","No field can be empty");
        }

        Pattern pattern = Pattern.compile("\\D");
        Matcher matcher = pattern.matcher(addressEntity.getPincode());

        if(addressEntity.getPincode().length() < 6 || matcher.find()){
            throw new SaveAddressException("SAR-002","Invalid pincode");
        }


        addressDao.saveAddress(addressEntity);

        return addressEntity;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getStateEntityByUUID(String uuid) throws AddressNotFoundException {

        StateEntity stateEntity = addressDao.getStateByUUID(uuid);

        if(stateEntity==null){
            throw new AddressNotFoundException("ANF-002","No state by this id");
        }

        return stateEntity;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AddressEntity> getAddresses(String authorization) throws AuthorizationFailedException {

        String[] bearerToken = authorization.split("Bearer ");
        CustomerAuthEntity customerAuthEntity = customerDao.getCusotmerAuth(bearerToken[1]);
        if(customerAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime now = ZonedDateTime.now();
        if(now.isAfter(customerAuthEntity.getExpiresAt())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        }

        List<AddressEntity> addressEntities = addressDao.getAddresses();

        Collections.sort(addressEntities, new Comparator<AddressEntity>() {
            @Override
            public int compare(AddressEntity o1, AddressEntity o2) {
                return (o1.getId() <= o2.getId())? -1 : 1;
            }
        });

        return addressEntities;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getAddressEntityByUUID(String uuid){
        return addressDao.getAddressesByUUID(uuid);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(String authorization,AddressEntity addressEntity) throws AuthorizationFailedException, AddressNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        CustomerAuthEntity customerAuthEntity = customerDao.getCusotmerAuth(bearerToken[1]);
        if(customerAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime now = ZonedDateTime.now();
        if(now.isAfter(customerAuthEntity.getExpiresAt())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        }

        if(addressEntity.getUuid()==null || addressEntity.getUuid()==""){
            throw new AddressNotFoundException("ANF-005","Address id can not be empty");
        }

        AddressEntity tobeDeletedAddress = getAddressEntityByUUID(addressEntity.getUuid());

        if(tobeDeletedAddress==null){
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }

        List<AddressEntity> addressEntities = addressDao.getAddresses(customerAuthEntity.getCustomerEntity());

        if(!addressEntities.contains(tobeDeletedAddress)){
            throw new AuthorizationFailedException("ATHR-004","You are not authorized to view/update/delete any one else's address");
        }

        addressDao.deleteAddress(tobeDeletedAddress);

        return tobeDeletedAddress;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<StateEntity> getStates(){
        return addressDao.getStates();
    }




}
