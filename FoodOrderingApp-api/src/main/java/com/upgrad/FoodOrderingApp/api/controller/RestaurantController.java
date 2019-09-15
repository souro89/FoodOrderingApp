package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class RestaurantController {


    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ItemService itemService;

    /*Get Restaurant Details*/
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> restaurantsByRating() {

        List<RestaurantEntity> restaurantList = restaurantService.restaurantsByRating();

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        for (RestaurantEntity restaurantEntity : restaurantList) {
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntity.getAddressEntity().getStateEntity().getUuid()))
                    .stateName(restaurantEntity.getAddressEntity().getStateEntity().getStateName());

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddressEntity().getUuid()))
                    .city(restaurantEntity.getAddressEntity().getCity())
                    .flatBuildingName(restaurantEntity.getAddressEntity().getFaltBuilNumber())
                    .locality(restaurantEntity.getAddressEntity().getLocality())
                    .pincode(restaurantEntity.getAddressEntity().getPincode())
                    .state(restaurantDetailsResponseAddressState);

            String categories = "";

            List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());

            Collections.sort(categoryEntities, new Comparator<CategoryEntity>() {
                @Override
                public int compare(CategoryEntity o1, CategoryEntity o2) {
                    return o1.getCategoryName().compareTo(o2.getCategoryName());
                }
            });

            for (CategoryEntity categoryEntity : categoryEntities) {
                categories += categoryEntity.getCategoryName() + ", ";
            }

//            if(categories != null)
//                categories = categories.substring(0,categories.length()-2);

            RestaurantList restaurantList1 = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .customerRating(restaurantEntity.getCustomerRating())
                    .averagePrice(restaurantEntity.getAveragePriceForTwo())
                    .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                    .address(restaurantDetailsResponseAddress)
                    .categories(categories);

            restaurantListResponse.addRestaurantsItem(restaurantList1);

        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    /*Get restaurant details by Name*/
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> restaurantsByName(@PathVariable("restaurant_name") String restaurantName) throws RestaurantNotFoundException {


        List<RestaurantEntity> restaurantEntityList = restaurantService.restaurantsByName(restaurantName);

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        if (restaurantEntityList.isEmpty()) {
            return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
        }


        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntity.getAddressEntity().getStateEntity().getUuid()))
                    .stateName(restaurantEntity.getAddressEntity().getStateEntity().getStateName());

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddressEntity().getUuid()))
                    .city(restaurantEntity.getAddressEntity().getCity())
                    .flatBuildingName(restaurantEntity.getAddressEntity().getFaltBuilNumber())
                    .locality(restaurantEntity.getAddressEntity().getLocality())
                    .pincode(restaurantEntity.getAddressEntity().getPincode())
                    .state(restaurantDetailsResponseAddressState);

            String categories = "";

            List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());

            Collections.sort(categoryEntities, new Comparator<CategoryEntity>() {
                @Override
                public int compare(CategoryEntity o1, CategoryEntity o2) {
                    return o1.getCategoryName().compareTo(o2.getCategoryName());
                }
            });

            for (CategoryEntity categoryEntity : categoryEntities) {
                categories += categoryEntity.getCategoryName() + ", ";
            }

            try {
                categories = categories.substring(0, categories.length() - 2);
            } catch (StringIndexOutOfBoundsException exe) {
                categories = "";
            }


            RestaurantList restaurantList1 = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .customerRating(restaurantEntity.getCustomerRating())
                    .averagePrice(restaurantEntity.getAveragePriceForTwo())
                    .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                    .address(restaurantDetailsResponseAddress)
                    .categories(categories);

            restaurantListResponse.addRestaurantsItem(restaurantList1);

        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);

    }


    /*get Restaurant details by category*/
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> restaurantByCategory(@PathVariable("category_id") String categoryId) throws CategoryNotFoundException {

//        if(categoryId == null || categoryId == ""){
//            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
//        }

        List<RestaurantEntity> restaurantList = restaurantService.restaurantByCategory(categoryId);

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        for (RestaurantEntity restaurantEntity : restaurantList) {
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntity.getAddressEntity().getStateEntity().getUuid()))
                    .stateName(restaurantEntity.getAddressEntity().getStateEntity().getStateName());

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddressEntity().getUuid()))
                    .city(restaurantEntity.getAddressEntity().getCity())
                    .flatBuildingName(restaurantEntity.getAddressEntity().getFaltBuilNumber())
                    .locality(restaurantEntity.getAddressEntity().getLocality())
                    .pincode(restaurantEntity.getAddressEntity().getPincode())
                    .state(restaurantDetailsResponseAddressState);

            String categories = "";

            List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());

            Collections.sort(categoryEntities, new Comparator<CategoryEntity>() {
                @Override
                public int compare(CategoryEntity o1, CategoryEntity o2) {
                    return o1.getCategoryName().compareTo(o2.getCategoryName());
                }
            });

            for (CategoryEntity categoryEntity : categoryEntities) {
                categories += categoryEntity.getCategoryName() + ", ";
            }

//            if(categories != null || categories!="" || categories.length() > 2)
//                categories = categories.substring(0,categories.length()-2);

            RestaurantList restaurantList1 = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .customerRating(restaurantEntity.getCustomerRating())
                    .averagePrice(restaurantEntity.getAveragePriceForTwo())
                    .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                    .address(restaurantDetailsResponseAddress)
                    .categories(categories);

            restaurantListResponse.addRestaurantsItem(restaurantList1);

        }

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);

    }

    /*Get Restaurant details by UUID*/
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantById(@PathVariable("restaurant_id") String restaurantId
    ) throws AuthorizationFailedException, InvalidRatingException, RestaurantNotFoundException, CategoryNotFoundException {

//        if(restaurantId==null || restaurantId==""){
//            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
//        }

        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);

//        if(restaurantEntity == null){
//            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
//        }


        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                .id(UUID.fromString(restaurantEntity.getAddressEntity().getStateEntity().getUuid()))
                .stateName(restaurantEntity.getAddressEntity().getStateEntity().getStateName());

        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                .id(UUID.fromString(restaurantEntity.getAddressEntity().getUuid()))
                .city(restaurantEntity.getAddressEntity().getCity())
                .flatBuildingName(restaurantEntity.getAddressEntity().getFaltBuilNumber())
                .locality(restaurantEntity.getAddressEntity().getLocality())
                .pincode(restaurantEntity.getAddressEntity().getPincode())
                .state(restaurantDetailsResponseAddressState);


        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse()
                .id(UUID.fromString(restaurantEntity.getUuid()))
                .restaurantName(restaurantEntity.getRestaurantName())
                .photoURL(restaurantEntity.getPhotoUrl())
                .customerRating(restaurantEntity.getCustomerRating())
                .averagePrice(restaurantEntity.getAveragePriceForTwo())
                .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                .address(restaurantDetailsResponseAddress);

        CategoryList categoryList = new CategoryList();

        for (CategoryEntity categoryEntity : categoryService.getCategoriesByRestaurant(restaurantId)) {

            categoryList = new CategoryList()
                    .id(UUID.fromString(categoryEntity.getUuid()))
                    .categoryName(categoryEntity.getCategoryName())
            ;

            List<ItemEntity> itemEntities = itemService.getItemsByCategoryAndRestaurant(restaurantId, categoryEntity.getUuid());

            for (ItemEntity itemEntity : itemEntities) {
                ItemList itemList = new ItemList()
                        .id(UUID.fromString(itemEntity.getUuid()))
                        .itemName(itemEntity.getItemName())
                        .price(itemEntity.getPrice())
                        .itemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType().getValue()));

                categoryList.addItemListItem(itemList);
            }

            restaurantDetailsResponse.addCategoriesItem(categoryList);
        }

        return new ResponseEntity<RestaurantDetailsResponse>(restaurantDetailsResponse, HttpStatus.OK);


    }

    /*Update the rating for the restaurant*/
    @RequestMapping(method = RequestMethod.PUT, path = "/restaurant/{restaurant_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(
            @RequestHeader("authorization") String authorization,
            @RequestParam("customer_rating") Double customerRating,
            @PathVariable("restaurant_id") String restaurantId) throws AuthorizationFailedException, InvalidRatingException, RestaurantNotFoundException {


        String[] accessToken = authorization.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(accessToken[1]);

        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);

//        if(customerRating < 1.0 ||  customerRating > 5.0){
//            throw new InvalidRatingException("IRE-001","Restaurant should be in the range of 1 to 5");
//        }

        RestaurantEntity updatedRestaurantEntity = restaurantService.updateRestaurantRating(restaurantEntity, customerRating);

        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse()
                .id(UUID.fromString(restaurantId))
                .status("RESTAURANT RATING UPDATED SUCCESSFULLY");
        return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse, HttpStatus.OK);

    }


}
