package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {

    @Autowired
    private CouponDao couponDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByName(String couponName) throws CouponNotFoundException {
        if(couponName == null || couponName == ""){
            throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }

        CouponEntity couponEntity = couponDao.getCouponByName(couponName);

        if(couponEntity == null){
            throw new CouponNotFoundException("CPF-001","No coupon by this name");
        }

        return couponEntity;

    }

}
