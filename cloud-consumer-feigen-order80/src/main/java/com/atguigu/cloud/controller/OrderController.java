package com.atguigu.cloud.controller;
import cn.hutool.core.date.DateUtil;
import com.atguigu.cloud.apis.PayFeignApi;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
public class OrderController {
    @Resource
    private PayFeignApi payFeignApi;

    @PostMapping(value = "/feign/pay/add")
    public ResultData addOrder(@RequestBody PayDTO payDTO){
        System.out.println("第一步:模拟本地addOrder新增订单成功（省略sql操作）");
        System.out.println("第二步:再开启addPay支付微服务远程调用");
        ResultData resultData = payFeignApi.addPay(payDTO);
        return resultData;
    }
    @GetMapping(value = "/feign/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id) throws InterruptedException {
        System.out.println("支付微服务调用，按照id查询订单支付流水信息开始启动");
        ResultData resultData= null;
        try {
            System.out.println("调用开始"+ DateUtil.now());
            resultData = payFeignApi.getPayInfo(id);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("调用结束"+ DateUtil.now());
            ResultData.fail(ReturnCodeEnum.RC500.getCode(),e.getMessage());
        }
        return resultData;
    }
    @GetMapping(value = "/feign/pay/mylb")
    public String mylb(){
        return payFeignApi.mylb();

    }

}
