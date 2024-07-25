package com.atguigu.cloud.controller;
import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@Tag(name="支付微服务模块",description = "支付CRUD")
public class PayController {
    @Resource
    private PayService payService;
    //增加
    @PostMapping("/pay/add")
    @Operation(summary = "新增",description = "新增支付流水,json串做参数")
    public ResultData<String> addPay(@RequestBody Pay pay){
    System.out.println(pay.toString());
    int i=payService.add(pay);
    return ResultData.success("成功插入记录，返回值为："+i);
    }
    //删除
    @DeleteMapping("/pay/del/{id}")
    @Operation(summary = "删除",description = "删除支付流水方法")
    public ResultData<String> deletePay(@PathVariable("id") Integer id){
        int i=payService.delete(id);
        return ResultData.success("成功修改记录，返回值为"+i);
    }
    //更改
    @PutMapping(value="/pay/update")
    @Operation(summary = "修改",description = "修改支付流水方法")
    public ResultData<String> updatePay(@RequestBody PayDTO payDTO){
        Pay pay = new Pay();
        BeanUtils.copyProperties(payDTO,pay);
        int i = payService.update(pay);
        return ResultData.success("成功修改记录，返回值为："+1);
    }
    //查询
    @GetMapping(value = "/pay/get/{id}")
    @Operation(summary = "按照ID查询流水",description = "查询支付流水方法")
    public ResultData<Pay> getId(@PathVariable("id") Integer id){
        if(id == -4) throw new RuntimeException("id不能为负数");
        //暂停62秒钟线程
        try {TimeUnit.SECONDS.sleep(62);}catch (InterruptedException e){e.printStackTrace();}
        Pay pay = payService.getById(id);
        return ResultData.success(pay);
    }
    //查询全部
    @GetMapping("/pay/getAll")
    public ResultData<List> getAll(){
        List<Pay> pays = payService.getAll();
        return ResultData.success(pays);
    }
    @Value("${server.port}")
    private String port;
    @GetMapping(value = "/pay/get/info")
    public String getInfoByConsul(@Value("${atguigu.info}")String atguiguInfo){
        return "atguiguInfo:"+atguiguInfo+"port:"+port;
    }


}
