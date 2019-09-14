package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {


    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity restaurantByUUID(String someRestaurantId) throws RestaurantNotFoundException {

        RestaurantEntity restaurantEntity = restaurantDao.restaurantByUUID(someRestaurantId);

        if(restaurantEntity == null){
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }

        return restaurantEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantsByName(String someRestaurantName) throws RestaurantNotFoundException {



        if(someRestaurantName == "" || someRestaurantName == null){
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");
        }



        return restaurantDao.restaurantsByName(someRestaurantName);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantsByRating(){
        return restaurantDao.getAllRestaurants();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantByCategory(String uuid) throws CategoryNotFoundException {

        CategoryEntity categoryEntity = categoryDao.categoryByUUID(uuid);

        if(categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }

        List<RestaurantEntity> restaurantEntities = restaurantDao.getAllRestaurants();

        List<RestaurantEntity> finalRestaurantList = new ArrayList<>();

        for(RestaurantEntity restaurantEntity : restaurantEntities){
            if(restaurantEntity.getCategories().contains(categoryEntity)){
                finalRestaurantList.add(restaurantEntity);
            }
        }

        return finalRestaurantList;

    }

    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity,Double rating) throws InvalidRatingException {

        if(rating < 1.0 ||  rating > 5.0){
            throw new InvalidRatingException("IRE-001","Restaurant should be in the range of 1 to 5");
        }

        BigDecimal oldRating = restaurantEntity.getCustomerRating();
        BigDecimal newRating = oldRating.add(new BigDecimal(rating));

        newRating = newRating.divide(new BigDecimal(restaurantEntity.getNumberOfCustomersRated()+1));

        restaurantEntity.setCustomerRating(newRating );
        restaurantEntity.setNumberOfCustomersRated(restaurantEntity.getNumberOfCustomersRated()+1);

        restaurantDao.updateRestaurant(restaurantEntity);

        return restaurantEntity;

    }
}
