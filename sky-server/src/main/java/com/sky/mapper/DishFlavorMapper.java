package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param dishFlavors
     */
    void addDishFlavors(List<DishFlavor> dishFlavors);

    void deleteDishFlavorsByDishIds(List<Long> ids);

    /**
     * 根据菜品id查询口味数据
     * @param id
     * @return
     */
    @Select("SELECT * FROM dish_flavor WHERE dish_id = #{id}")
    List<DishFlavor> selectByDishId(Long id);
}
