package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.dao.OrdersDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemService {


    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ItemDao itemDao;

    public List<ItemEntity> getItemsByCategoryAndRestaurant(String someRestaurantId, String uuid) throws RestaurantNotFoundException, CategoryNotFoundException {

        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(someRestaurantId);
        CategoryEntity categoryEntity = categoryService.categoryByUUID(uuid);
        List<ItemEntity> itemsByRestaurantAndCategory = new ArrayList<>();

        for(ItemEntity itemEntity : restaurantEntity.getItems()){
            for(ItemEntity itemEntity1 : categoryEntity.getItems()){
                if(itemEntity.getUuid().equals(itemEntity1.getUuid())){
                    itemsByRestaurantAndCategory.add(itemEntity);
                }
            }
        }

        itemsByRestaurantAndCategory.sort(new Comparator<ItemEntity>() {
            @Override
            public int compare(ItemEntity o1, ItemEntity o2) {
                return o1.getItemName().toLowerCase().compareTo(o2.getItemName().toLowerCase());
            }
        });

        return itemsByRestaurantAndCategory;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity){
        List<ItemEntity> itemEntities = new ArrayList<>();
        for(OrderEntity orderEntity : ordersDao.getOrdersByRestaurant(restaurantEntity)){
            for(OrderItemEntity  orderItemEntity: orderItemDao.getItemsByOrder(orderEntity)){
                itemEntities.add(orderItemEntity.getItemEntity());
            }
        }

        Map<String,Integer> map = new HashMap<>();
        for(ItemEntity itemEntity : itemEntities){
            Integer count=map.get(itemEntity.getUuid());
            map.put(itemEntity.getUuid(),(count == null) ? 1 : count + 1);
        }

        Map<String,Integer> treeMap = new TreeMap<>(map);
        List<ItemEntity> sortedItemList = new ArrayList<>();
        for(Map.Entry<String,Integer> entry : treeMap.entrySet()){
            sortedItemList.add(itemDao.getItemByUUID(entry.getKey()));
        }
        Collections.reverse(sortedItemList);
        return sortedItemList;
    }

    public List<OrderItemEntity> getItemsByOrder(OrderEntity orderEntity){
        return orderItemDao.getItemsByOrder(orderEntity);
    }

}
