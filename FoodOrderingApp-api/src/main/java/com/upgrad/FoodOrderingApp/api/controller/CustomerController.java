package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@CrossOrigin
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @RequestMapping(method = RequestMethod.POST,path="/customer/signup",consumes= MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false)final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

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
      return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.POST,path = "/customer/login",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorozation") String authorization) throws AuthenticationFailedException {

        CustomerAuthEntity customerAuthEntity = customerService.loginService(authorization);
        CustomerEntity customerEntity = customerAuthEntity.getCustomerEntity();
        LoginResponse loginResponse =  new LoginResponse().id(customerEntity.getUuid()).firstName(customerEntity.getFirstName())
                                    .lastName(customerEntity.getLastName()).contactNumber(customerEntity.getContactNumber())
                                    .emailAddress(customerEntity.getEmail()).message("LOGGED IN SUCCESSFULLY");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access-token",customerAuthEntity.getAccessToken());
        return new ResponseEntity<LoginResponse>(loginResponse,httpHeaders,HttpStatus.OK);

    }





}
