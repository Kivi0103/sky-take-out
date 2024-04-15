package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 添加菜品及其口味
     * @param dishDTO
     */
    @Override
    @Transactional
    public void addDishAndFlavor(DishDTO dishDTO) {

        // 将DTO类转为实体类
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 从Dto类中获取口味
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();

        // xml映射文件中使用useGeneratedKeys="true"，自动设置了id
        dishMapper.addDish(dish);

        for (DishFlavor dishFlavor : dishFlavors) {
            dishFlavor.setDishId(dish.getId());
        }

        if (dishFlavors!= null && dishFlavors.size() > 0) {
            dishFlavorMapper.addDishFlavors(dishFlavors);
        }
    }
}
