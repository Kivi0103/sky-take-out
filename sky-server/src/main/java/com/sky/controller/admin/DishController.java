package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "后台菜品管理")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.addDishAndFlavor(dishDTO);
        return Result.success();
    }


    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> pageDish(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询菜品：{}", dishPageQueryDTO);
        return Result.success(dishService.pageDish(dishPageQueryDTO));
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteDish(@RequestParam List<Long> ids){// @RequestParam 注解是mvc提供的注解，用来接收请求参数并自动绑定到方法参数中
        log.info("批量删除菜品：{}"+ids.get(0));
        dishService.deleteDish(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("根据id查询菜品：{}", id);
        return Result.success(dishService.getDishAndFlavorById(id));
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}", dishDTO);
        dishService.updateDishAndFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品启售停售
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品启售停售")
    public Result updateDishStatus(@PathVariable Integer status, @RequestParam Long id){
        log.info("菜品启售停售：{}，ids：{}", status, id);
        dishService.updateDishStatus(status, id);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品列表
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品列表")
    public Result<List<Dish>> listDishByCategoryId(@RequestParam Long categoryId){
        log.info("根据分类id查询菜品列表：{}", categoryId);
        List<Dish> dishes = dishService.listDishByCategoryId(categoryId);
        return Result.success(dishes);
    }
}
