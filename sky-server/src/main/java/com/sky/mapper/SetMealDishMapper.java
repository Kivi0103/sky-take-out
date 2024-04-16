package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    /**
     * 根据菜品id查询套餐id
     * @param ids
     */
    List<Long> selectByDishIds(List<Long> ids);

    /**
     * 根据分类id查询套餐中的菜品
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getSetmealDishes(Long id);

    @Insert("insert into setmeal_dish(setmeal_id, dish_id, name, price, copies) values(#{setmealId}, #{dishId}, #{name}, #{price}, #{copies})")
    void add(SetmealDish setmealDish);

    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetmealId(Long id);

    @Select("select sd.name,sd.copies,d.image,d.description from setmeal_dish sd left join dish d on sd.dish_id = d.id where setmeal_id = #{id}")
    List<DishItemVO> getDishItemBySetmealId(Long id);
}
