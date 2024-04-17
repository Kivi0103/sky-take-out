package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
@Api(tags = "C端-购物车接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param dto
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO dto) {
        log.info("添加购物车：{}", dto);
        shoppingCartService.addShoppingCart(dto);
        return Result.success();
    }

    /**
     * 显示购物车
     * @param
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("显示购物车")
    public Result<List<ShoppingCart>> showShoppingCart() {
        log.info("显示购物车");
        return Result.success(shoppingCartService.showShoppingCart());
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result cleanShoppingCart() {
        log.info("清空购物车");
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    @PostMapping("/sub")
    @ApiOperation("删除一个购物车商品")
    public Result deleteShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除一个购物车商品：{}", shoppingCartDTO);
        shoppingCartService.deleteShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
