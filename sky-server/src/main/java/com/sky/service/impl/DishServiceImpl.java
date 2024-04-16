package com.sky.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;
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

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageDish(DishPageQueryDTO dishPageQueryDTO) {
        // pageHelper
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        // 根据分类id，状态，名字动态查询
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        // 封装成pageResult
        return new PageResult(page.getTotal(), page.getResult());

    }

    /**
     * 根据id批量删除菜品及其口味
     * @param ids
     */
    @Override
    @Transactional
    public void deleteDish(List<Long> ids) {
        // 判断菜品是否是启售？
        for (Long id : ids) {
            Dish dish = dishMapper.selecById(id);
            if (dish.getStatus() == 1) {// 启售状态
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断菜品是否绑定了套餐
        List<Long> setMealDishIds = setMealDishMapper.selectByDishIds(ids);
        if (!setMealDishIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除
        dishMapper.deleteDishByIds(ids);

        // 删除口味
        dishFlavorMapper.deleteDishFlavorsByDishIds(ids);
    }

    /**
     * 根据id查询菜品及其口味
     * @param id
     * @return
     */
    @Override
    public DishVO getDishAndFlavorById(Long id) {
        // 根据id查询菜品
        Dish dish = dishMapper.selecById(id);
        // 将实体类转为VO类
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        // 根据菜品id查询口味
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectByDishId(id);

        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 更改菜品信息
     * @param dishDTO
     */
    @Override
    @Transactional
    public void updateDishAndFlavor(DishDTO dishDTO) {
        // 修改菜品表中的菜品信息，根据菜品id
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);

        // 将DTO中的菜品id取出来封装成一个List<Long集合

        List<Long> dishIds = new ArrayList<>();
        dishIds.add(dishDTO.getId());
        // 删除口味表中所有原口味
        dishFlavorMapper.deleteDishFlavorsByDishIds(dishIds);

        // 将新传进来的菜品口味信息添加进口味表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDTO.getId());
        }
        dishFlavorMapper.addDishFlavors(flavors);
    }

    @Override
    public void updateDishStatus(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.updateDish(dish);
    }

    @Override
    public List<Dish> listDishByCategoryId(Long categoryId) {
        List<Dish> dishList = dishMapper.selectByCategoryId(categoryId);

        return dishList;
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);
        List<DishVO> dishVOList = new ArrayList<>();
        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);
            List<DishFlavor> dishFlavors = dishFlavorMapper.selectByDishId(d.getId());
            dishVO.setFlavors(dishFlavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }
}
