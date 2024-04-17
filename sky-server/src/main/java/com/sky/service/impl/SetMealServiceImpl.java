package com.sky.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    public SetmealVO geById(Long id) {
        log.info("根据id查询套餐信息");
        // 从套餐表中查询套餐信息
        Setmeal setmeal = setMealMapper.getById(id);

        // 如果setmeal为空，则说明套餐不存在
        if (setmeal == null) {
            return null;
        }

        // 根据套餐id从套餐菜品表中把对应的菜品都查询出来
        List<SetmealDish> setmealDishes = setMealDishMapper.getSetmealDishes(id);

        // 封装套餐信息和菜品信息到VO对象中
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void add(SetmealDTO setmealDTO) {
        log.info("service层...新增套餐");
        // 将套餐信息加入套餐表中
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.add(setmeal);

        // 将套餐菜品信息加入套餐菜品表中
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
            setMealDishMapper.add(setmealDish);
        }
    }

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("service层...分页查询套餐");
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<Setmeal> page = setMealMapper.page(setmealPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());

        return pageResult;
    }


    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        log.info("service层...更新套餐");
        // 更新套餐信息
        // 将套餐信息加入套餐表中
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.update(setmeal);

        // 删除原有的套餐菜品信息
        setMealDishMapper.deleteBySetmealId(setmeal.getId());
        // 将新的套餐菜品信息加入套餐菜品表中
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
            setMealDishMapper.add(setmealDish);
        }

    }

    @Override
    public void modifyStatus(Integer status, Long id) {
        setMealMapper.modifyStatus(status, id);
    }

    @Override
    public void delete(List<Long> ids) {
        // 需要删除的套餐是否是启售状态
        for (Long id : ids) {
            Setmeal setmeal = setMealMapper.getById(id);
            if (setmeal.getStatus() == 1) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setMealMapper.delete(ids);
    }

    /**
     * 动态条件查询
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
       List<Setmeal> list = setMealMapper.list(setmeal);
       return list;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        List<DishItemVO> dishesBySetmealId = setMealDishMapper.getDishItemBySetmealId(id);

        return dishesBySetmealId;
    }
}
