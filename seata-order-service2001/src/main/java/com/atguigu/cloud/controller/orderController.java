package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.Order;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class orderController {

    @Resource
    private OrderService orderService;
    /*
     *创建订单
     * */
    @GetMapping("/order/create")
    public ResultData create(Order order){
        orderService.creat(order);
        return ResultData.success(order);
    }
}
