package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerRequest;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestControllerException {

    public ResponseEntity<ErrorResponse> signupFailed(SignUpRestrictedException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
            new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.FORBIDDEN
        );
    }

    public ResponseEntity<ErrorResponse> loginFailed(AuthenticationFailedException exe,WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.UNAUTHORIZED
        );
    }

    public ResponseEntity<ErrorResponse> logoutFailed(AuthorizationFailedException exe,WebRequest request){
        return new ResponseEntity<ErrorResponse>(
          new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND
        );
    }

    public ResponseEntity<ErrorResponse> updateCustomerEntityFailed(UpdateCustomerException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND
        );
    }

    public ResponseEntity<ErrorResponse> saveAddressException(SaveAddressException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND
        );
    }

    public ResponseEntity<ErrorResponse> addressNotFound(AddressNotFoundException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND
        );
    }


}
