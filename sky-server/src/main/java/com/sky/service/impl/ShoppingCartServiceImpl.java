package com.sky.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private SetMealMapper setMealMapper;
    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车：{}",shoppingCartDTO);
        // 判断该信息是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if(list != null && list.size() > 0){
            // 存在，则更新数量
            ShoppingCart shoppingCart1 = list.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartMapper.updateNumberById(shoppingCart1);
        }else{
            // 不存在，则新增// 如果菜品id不为空，则表示添加菜品进购物车
            if (shoppingCartDTO.getDishId()!= null){
                Long dishId = shoppingCartDTO.getDishId();
                // 从数据库中查出菜品
                Dish dish = dishMapper.selecById(dishId);
                // 设置shoppingCart的属性
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setDishId(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
            }else{
                // 如果菜品id为空，则表示添加套餐进购物车
                Long setMealId = shoppingCartDTO.getSetmealId();
                // 从数据库中查出套餐
                Setmeal setmeal = setMealMapper.getById(setMealId);
                // 设置shoppingCart的属性
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setSetmealId(setMealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            // 新增到购物车数据库
            shoppingCartMapper.insert(shoppingCart);
        }


    }
}
