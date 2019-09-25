package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    /*save an address*/
    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization") String authorization,
                                                           @RequestBody(required = false) SaveAddressRequest saveAddressRequest) throws AddressNotFoundException, AuthorizationFailedException, SaveAddressException {


        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setFaltBuilNumber(saveAddressRequest.getFlatBuildingName());
        addressEntity.setStateEntity(addressService.getStateByUUID(saveAddressRequest.getStateUuid()));
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());

        AddressEntity savedAddress = addressService.saveAddress(addressEntity, customerEntity);
        SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(savedAddress.getUuid())
                .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);
    }

    /*get addresses for a customer*/
    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAddress(@RequestHeader("authorization") String authorization) throws AuthorizationFailedException {

        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);


        // Get all addresses
        List<AddressEntity> addressesList = addressService.getAddresses(customerEntity);

        System.out.println("asdasd"+addressesList.get(0));

        // saved addresses to address list variable
        AddressListResponse addressListResponse = new AddressListResponse();

        for (AddressEntity addressEntity : addressesList) {
            AddressList addressesResponse = new AddressList()
                    .id(UUID.fromString(addressEntity.getUuid()))
                    .flatBuildingName(addressEntity.getFaltBuilNumber())
                    .locality(addressEntity.getLocality())
                    .city(addressEntity.getCity())
                    .pincode(addressEntity.getPincode())
                    .state(new AddressListState().id(UUID.fromString(addressEntity.getStateEntity().getUuid()))
                            .stateName(addressEntity.getStateEntity().getStateName()));
            addressListResponse.addAddressesItem(addressesResponse);
        }

        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    /* to retrieve all states in db*/
    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {
        // Get all states
        List<StateEntity> statesList = addressService.getStates();


        StatesListResponse statesListResponse = new StatesListResponse();

        for (StateEntity stateEntity : statesList) {
            StatesList listState = new StatesList()
                    .id(UUID.fromString(stateEntity.getUuid()))
                    .stateName(stateEntity.getStateName());
            statesListResponse.addStatesItem(listState);
        }

        return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
    }

    /* Delete the address if the customer is authorized based on access token check*/
    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}")
    public ResponseEntity<DeleteAddressResponse> deleteSavedAddress(
            @PathVariable("address_id") final String addressID,
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, AddressNotFoundException {
        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        if (addressID.equals("")) {
            throw new AddressNotFoundException("ANF-005", "Address id can not be empty");
        }

        AddressEntity addressEntity = addressService.getAddressByUUID(addressID, customerEntity);
        AddressEntity deletedAddressEntity = addressService.deleteAddress(addressEntity);
        DeleteAddressResponse addDeleteResponse = new DeleteAddressResponse().id(UUID.fromString(deletedAddressEntity.getUuid())).status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(addDeleteResponse, HttpStatus.OK);
    }


}
