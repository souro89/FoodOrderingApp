package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.Base64;
import java.util.UUID;

@Controller
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /*Customer Sign up*/
    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

        if (signupCustomerRequest.getFirstName() == null || signupCustomerRequest.getFirstName() == ""
                || signupCustomerRequest.getContactNumber() == null || signupCustomerRequest.getContactNumber() == ""
                || signupCustomerRequest.getEmailAddress() == null || signupCustomerRequest.getEmailAddress() == ""
                || signupCustomerRequest.getPassword() == null || signupCustomerRequest.getPassword() == ""
        ) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }


        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setSalt("CheckCheck");
        customerEntity.setPassword(signupCustomerRequest.getPassword());

        CustomerEntity newCustomerEntity = customerService.saveCustomer(customerEntity);
        SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id(newCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);


    }

    /*Customer login*/
    @RequestMapping(method = RequestMethod.POST, path = "/customer/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") String authorization) throws AuthenticationFailedException {


        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray;
        String contactNumber;
        String password;
        try {
            decodedArray = decodedText.split(":");
            contactNumber = decodedArray[0];
            password = decodedArray[1];
        } catch (ArrayIndexOutOfBoundsException exe) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }


        CustomerAuthEntity customerAuthEntity = customerService.loginService(decodedArray[0], decodedArray[1]);
        CustomerEntity customerEntity = customerAuthEntity.getCustomerEntity();
        LoginResponse loginResponse = new LoginResponse().id(customerEntity.getUuid()).firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName()).contactNumber(customerEntity.getContactNumber())
                .emailAddress(customerEntity.getEmail()).message("LOGGED IN SUCCESSFULLY");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access-token", customerAuthEntity.getAccessToken());
        return new ResponseEntity<LoginResponse>(loginResponse, httpHeaders, HttpStatus.OK);

    }

    /*Customer logout*/
    @RequestMapping(method = RequestMethod.POST, path = "/customer/logout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") String authorization) throws AuthorizationFailedException {

        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);


        CustomerAuthEntity customerAuthEntity = customerService.logout(bearerToken[1]);
        LogoutResponse logoutResponse = new LogoutResponse().id(customerAuthEntity.getCustomerEntity().getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
    }


    /*Update customer details*/
    @RequestMapping(method = RequestMethod.PUT, path = "/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> updateCustomerEntity(@RequestHeader("authorization") String authorization,
                                                                       @RequestBody(required = false) UpdateCustomerRequest updateCustomerRequest) throws AuthorizationFailedException, UpdateCustomerException {

        if (updateCustomerRequest.getFirstName() == null || updateCustomerRequest.getFirstName().isEmpty()
                || updateCustomerRequest.getLastName() == null || updateCustomerRequest.getLastName().isEmpty()) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }


        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);
        customerEntity.setFirstName(updateCustomerRequest.getFirstName());
        customerEntity.setLastName(updateCustomerRequest.getLastName());
        CustomerEntity updatedCustomerEntity = customerService.updateCustomerEntity(customerEntity);
        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse().id(updatedCustomerEntity.getUuid())
                .firstName(updatedCustomerEntity.getFirstName()).lastName(updatedCustomerEntity.getLastName());
        return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
    }

    /*Change customer password*/
    @RequestMapping(method = RequestMethod.PUT, path = "/customer/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> changePassword(@RequestHeader("authorization") String authorization,
                                                                 @RequestBody(required = false) UpdatePasswordRequest updatePasswordRequest) throws AuthorizationFailedException, UpdateCustomerException {

        if (updatePasswordRequest.getNewPassword() == null || updatePasswordRequest.getNewPassword().isEmpty()
                || updatePasswordRequest.getOldPassword() == null || updatePasswordRequest.getOldPassword().isEmpty()) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }

        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);
        System.out.println(customerEntity.getUuid());
        CustomerEntity updatedCustomerEntity = customerService.changePassword(updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword(), customerEntity);
        System.out.println(updatedCustomerEntity.getUuid() + " SS");
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse().id(updatedCustomerEntity.getUuid())
                .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);

    }


}
