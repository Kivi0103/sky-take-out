package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetMealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    @AutoFill(OperationType.INSERT)
    void add(Setmeal setmeal);

    Page<Setmeal> page(SetmealPageQueryDTO setmealPageQueryDTO);


    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @Update("update setmeal set status = #{status} where id = #{id}")
    void modifyStatus(Integer status, Long id);

    void delete(List<Long> ids);

    List<Setmeal> list(Setmeal setmeal);
}
