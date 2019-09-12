package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CouponService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class OrderController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @RequestMapping(method = RequestMethod.GET ,path = "/order/coupon/{coupon_name}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByName(@PathVariable("coupon_name") String couponName
                        , @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, CouponNotFoundException {

        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);

        CouponEntity couponEntity = couponService.getCouponByName(couponName);

        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().id(UUID.fromString(couponEntity.getUuid()))
                .couponName(couponEntity.getCouponName());

        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse,HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET,path = "/order",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CustomerOrderResponse> getOrdersbyCustomer(
            @RequestHeader("authorization") String authorization
    ) throws AuthorizationFailedException {
        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);

        List<OrderEntity> orderEntities = orderService.getOrdersbyCustomer(customerEntity);

        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();

        for(OrderEntity orderEntity : orderEntities){
            OrderList orderList = new OrderList();

            OrderListCoupon orderListCoupon = new OrderListCoupon()
                    .id(UUID.fromString(orderEntity.getCouponEntity().getUuid()))
                    .couponName(orderEntity.getCouponEntity().getCouponName())
                    .percent(orderEntity.getCouponEntity().getPercent());

            OrderListPayment orderListPayment = new OrderListPayment()
                    .id(UUID.fromString(orderEntity.getPaymentEntity().getUuid()))
                    .paymentName(orderEntity.getPaymentEntity().getPaymentName());

            OrderListCustomer orderListCustomer = new OrderListCustomer()
                    .id(UUID.fromString(orderEntity.getCustomerEntity().getUuid()))
                    .firstName(orderEntity.getCustomerEntity().getFirstName())
                    .lastName(orderEntity.getCustomerEntity().getLastName())
                    .emailAddress(orderEntity.getCustomerEntity().getEmail())
                    .contactNumber(orderEntity.getCustomerEntity().getContactNumber());

            OrderListAddressState orderListAddressState = new OrderListAddressState()
                    .id(UUID.fromString(orderEntity.getAddressEntity().getStateEntity().getUuid()))
                    .stateName(orderEntity.getAddressEntity().getStateEntity().getStateName());

            OrderListAddress orderListAddress = new OrderListAddress()
                    .id(UUID.fromString(orderEntity.getAddressEntity().getUuid()))
                    .flatBuildingName(orderEntity.getAddressEntity().getFaltBuilNumber())
                    .locality(orderEntity.getAddressEntity().getLocality())
                    .city(orderEntity.getAddressEntity().getCity())
                    .pincode(orderEntity.getAddressEntity().getPincode())
                    .state(orderListAddressState);

            orderList.id(UUID.fromString(orderEntity.getUuid()))
                    .bill(orderEntity.getBill())
                    .coupon(orderListCoupon)
                    .discount(orderEntity.getDiscount())
                    .date(orderEntity.getDate().toString())
                    .payment(orderListPayment)
                    .customer(orderListCustomer)
                    .address(orderListAddress);

            for (OrderItemEntity orderItemEntity : itemService.getItemsByOrder(orderEntity)) {

                ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem()
                        .id(UUID.fromString(orderItemEntity.getItemEntity().getUuid()))
                        .itemName(orderItemEntity.getItemEntity().getItemName())
                        .itemPrice(orderItemEntity.getItemEntity().getPrice())
                        .type(ItemQuantityResponseItem.TypeEnum.fromValue(orderItemEntity.getItemEntity().getType().getValue()));

                ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse()
                        .item(itemQuantityResponseItem)
                        .quantity(orderItemEntity.getQuantity())
                        .price(orderItemEntity.getPrice());

                orderList.addItemQuantitiesItem(itemQuantityResponse);
            }

            customerOrderResponse.addOrdersItem(orderList);

        }

        return new ResponseEntity<CustomerOrderResponse>(customerOrderResponse,HttpStatus.OK);

    }

}
