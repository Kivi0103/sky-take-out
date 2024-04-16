package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "店铺管理")
@RequestMapping("/admin/shop")
@Slf4j
public class AdminShopController {

    private static final String SHOP_STATUS_KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺营业状态
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setShopStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态：{}", status);
        // 利用string类型设置营业状态
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(SHOP_STATUS_KEY, status);
        return Result.success();
    }


    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getShopStatus() {
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS_KEY);
        log.info("获取店铺营业状态为:", shopStatus==1? "营业中" : "打烊中" );
        return Result.success(shopStatus);
    }
}
