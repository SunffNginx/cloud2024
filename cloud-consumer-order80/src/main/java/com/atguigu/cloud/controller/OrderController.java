package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class OrderController {
//    public static final String PaymentSrv_URL = "http://localhost:8001";//硬编码，不变扩展
        public static final String PaymentSrv_URL = "http://cloud-payment-service";//服务注册中心上的微服务名称

    @Resource
    RestTemplate restTemplate;
    //新增
    @GetMapping("/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO){
        return restTemplate.postForObject(PaymentSrv_URL+"/pay/add",payDTO,ResultData.class);
    }
    //查询
    @GetMapping("/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id){
        return restTemplate.getForObject(PaymentSrv_URL+"/pay/get/"+id,ResultData.class,id);
    }
    //修改
    @PutMapping("/consumer/pay/update")
    public ResultData UpdateOrder(PayDTO payDTO){
        return restTemplate.exchange(PaymentSrv_URL + "/pay/update", HttpMethod.PUT, new HttpEntity<>(payDTO), ResultData.class)
                .getBody();
    }
    //删除
    @DeleteMapping("/consumer/pay/del/{id}")
    public ResultData DeleteOrder(@PathVariable("id") Integer id){
        return restTemplate.exchange(PaymentSrv_URL + "/pay/del/" + id, HttpMethod.DELETE, null, ResultData.class).getBody();
    }
    @GetMapping("/consumer/pay/getAll")
    public ResultData getAllOrder(){
        return restTemplate.exchange(PaymentSrv_URL + "/pay/getAll", HttpMethod.GET, null, ResultData.class).getBody();
    }
    @GetMapping("consumer/pay/get/info")
    public String getInfoByConsul(){
        return restTemplate.getForObject(PaymentSrv_URL+"/pay/get/info",String.class);
    }
    @Resource
    private DiscoveryClient discoveryClient;
    @GetMapping("/consumer/discovery")
    public String discovery()
    {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }

        System.out.println("===================================");

        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId()+"\t"+element.getHost()+"\t"+element.getPort()+"\t"+element.getUri());
        }

        return instances.get(0).getServiceId()+":"+instances.get(0).getPort();
    }
}
