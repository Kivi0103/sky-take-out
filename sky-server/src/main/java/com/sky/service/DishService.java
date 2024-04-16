package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     * @return
     */
    void addDishAndFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageDish(DishPageQueryDTO dishPageQueryDTO);

    void deleteDish(List<Long> ids);

    DishVO getDishAndFlavorById(Long id);

    void updateDishAndFlavor(DishDTO dishDTO);

    void updateDishStatus(Integer status, Long id);

    List<Dish> listDishByCategoryId(Long categoryId);

    List<DishVO> listWithFlavor(Dish dish);
}
