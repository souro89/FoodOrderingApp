package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@CrossOrigin
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @RequestMapping(method = RequestMethod.POST,path="/customer/signup",consumes= MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignupCustomerResponse> signup(final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

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

}
