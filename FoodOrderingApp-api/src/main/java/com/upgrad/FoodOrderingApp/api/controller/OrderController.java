package com.upgrad.FoodOrderingApp.api.controller;

import com.sun.xml.internal.ws.wsdl.writer.document.http.Address;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class OrderController {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RestaurantService restaurantService;

    /*Get Coupon By Name*/
    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByName(@PathVariable("coupon_name") String couponName
            , @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, CouponNotFoundException {

        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);

        CouponEntity couponEntity = orderService.getCouponByName(couponName);

        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().id(UUID.fromString(couponEntity.getUuid()))
                .couponName(couponEntity.getCouponName()).percent(couponEntity.getPercent());

        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);

    }

    /*Get order by Customer*/
    @RequestMapping(method = RequestMethod.GET, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CustomerOrderResponse> getOrdersbyCustomer(
            @RequestHeader("authorization") String authorization
    ) throws AuthorizationFailedException {
        String[] bearerToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);

        List<OrderEntity> orderEntities = orderService.getOrdersByCustomers(customerEntity.getUuid());

        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();

        for (OrderEntity orderEntity : orderEntities) {
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

        return new ResponseEntity<CustomerOrderResponse>(customerOrderResponse, HttpStatus.OK);

    }


    /*Save order by customer*/
    @RequestMapping(method = RequestMethod.POST, path = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(
            @RequestBody(required = false) final SaveOrderRequest saveOrderRequest,
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, CouponNotFoundException,
            AddressNotFoundException, PaymentMethodNotFoundException,
            RestaurantNotFoundException, ItemNotFoundException {
        String accessToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        final OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUuid(UUID.randomUUID().toString());
        if(saveOrderRequest.getCouponId()!=null)
            orderEntity.setCouponEntity(orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString()));
        orderEntity.setPaymentEntity(paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString()));
        orderEntity.setCustomerEntity(customerEntity);
        orderEntity.setAddressEntity(addressService.getAddressByUUID(saveOrderRequest.getAddressId(), customerEntity));
        orderEntity.setBill(saveOrderRequest.getBill());
        orderEntity.setDiscount(saveOrderRequest.getDiscount());
        orderEntity.setRestaurant(restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString()));
        orderEntity.setDate(new Date());
        OrderEntity savedOrderEntity = orderService.saveOrder(orderEntity);

        for (ItemQuantity itemQuantity : saveOrderRequest.getItemQuantities()) {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setOrderEntity(savedOrderEntity);
            orderItemEntity.setItemEntity(itemService.getItemByUUID(itemQuantity.getItemId().toString()));
            orderItemEntity.setQuantity(itemQuantity.getQuantity());
            orderItemEntity.setPrice(itemQuantity.getPrice());
            orderService.saveOrderItem(orderItemEntity);
        }

        SaveOrderResponse saveOrderResponse = new SaveOrderResponse()
                .id(savedOrderEntity.getUuid()).status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse, HttpStatus.CREATED);
    }


}
