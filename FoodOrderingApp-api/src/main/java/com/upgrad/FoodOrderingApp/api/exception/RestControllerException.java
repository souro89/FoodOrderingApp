package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerRequest;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestControllerException {

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signupFailed(SignUpRestrictedException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
            new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> loginFailed(AuthenticationFailedException exe,WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> logoutFailed(AuthorizationFailedException exe,WebRequest request){
        return new ResponseEntity<ErrorResponse>(
          new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> updateCustomerEntityFailed(UpdateCustomerException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(SaveAddressException.class)
    public ResponseEntity<ErrorResponse> saveAddressException(SaveAddressException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> addressNotFound(AddressNotFoundException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> restaurantNotFound(RestaurantNotFoundException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFound(CategoryNotFoundException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<ErrorResponse> invalidRating(InvalidRatingException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> invalidRating(CouponNotFoundException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.BAD_REQUEST
        );
    }


}
