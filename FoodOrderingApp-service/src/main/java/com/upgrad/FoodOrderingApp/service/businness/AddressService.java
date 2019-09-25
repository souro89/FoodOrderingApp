package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
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

    @Autowired
    private CustomerAddressDao customerAddressDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(AddressEntity addressEntity, CustomerEntity customerEntity) throws AuthorizationFailedException, SaveAddressException {

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

        AddressEntity createdAddressEntity =  addressDao.saveAddress(addressEntity);

        CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
        customerAddressEntity.setCustomerEntity(customerEntity.getId());
        customerAddressEntity.setAddressEntity(createdAddressEntity.getId());
        customerAddressDao.createCustomerAddress(customerAddressEntity);



        return createdAddressEntity;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getStateByUUID(String uuid) throws AddressNotFoundException {

        StateEntity stateEntity = addressDao.getStateByUUID(uuid);

        if(stateEntity==null){
            throw new AddressNotFoundException("ANF-002","No state by this id");
        }

        return stateEntity;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AddressEntity> getAddresses(CustomerEntity customerEntity) throws AuthorizationFailedException {
        return customerEntity.getAddresses();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getAddressByUUID(String uuid,CustomerEntity customerEntity) throws AddressNotFoundException, AuthorizationFailedException {
        AddressEntity addressEntity = addressDao.getAddressesByUUID(uuid);

        if (addressEntity == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }

        if (!addressEntity.getCustomer().getUuid().equals(customerEntity.getUuid())) {
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }
        return addressEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(AddressEntity addressEntity) throws AuthorizationFailedException, AddressNotFoundException {

        if(addressEntity.getUuid()==null || addressEntity.getUuid()==""){
            throw new AddressNotFoundException("ANF-005","Address id can not be empty");
        }

        addressDao.deleteAddress(addressEntity);

        return addressEntity;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<StateEntity> getStates(){
        return addressDao.getStates();
    }




}
