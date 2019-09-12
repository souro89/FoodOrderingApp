package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RestaurantDao restaurantDao;


    public List<CategoryEntity> getCategoriesByRestaurant(String someRestaurantId) {

        RestaurantEntity restaurantEntity = restaurantDao.restaurantByUUID(someRestaurantId);
        return  restaurantEntity.getCategories().stream()
                    .sorted(Comparator.comparing(CategoryEntity::getCategoryName))
                    .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryEntity categoryByUUID(String uuid) throws CategoryNotFoundException {
        if(categoryDao.categoryByUUID(uuid)==null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        return categoryDao.categoryByUUID(uuid);
    }

    public List<CategoryEntity> getAllCategories() {



        return  categoryDao.getCategories();
    }

}
