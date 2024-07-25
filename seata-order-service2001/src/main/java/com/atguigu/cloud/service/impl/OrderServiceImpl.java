package com.atguigu.cloud.service.impl;

import com.atguigu.cloud.apis.AccountFeignApi;
import com.atguigu.cloud.apis.StorageFeignApi;
import com.atguigu.cloud.entities.Order;
import com.atguigu.cloud.mapper.OrderMapper;
import com.atguigu.cloud.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StorageFeignApi storageFeignApi;

    @Resource
    private AccountFeignApi accountFeignApi;

    @Override
    @GlobalTransactional(name="zzyy-create-order",rollbackFor = Exception.class)//AT
    public void creat(Order order) {
        //xid全局事务id的检查，重要
        String xid = RootContext.getXID();
        //1.新建订单
        log.info("------------------------开始新建订单" + "xid" + xid);
        //订单新建时默认初始订单状态是零
        order.setStatus(0);
        int result = orderMapper.insertSelective(order);
        //插入订单成功后获得插入mysql的实体对象
        Order orderFromDB = null;
        if (result > 0) {
            //从mysql里面查出来刚插入的记录
            orderFromDB = orderMapper.selectOne(order);
            log.info("------>开始新建订单,orderFromDB INFO:" + orderFromDB);
            System.out.println();
            System.out.println(orderFromDB.getUserId());
            //2.开始扣减积分
            log.info("------>订单微服务开始调用storage库存，做扣减count");
            storageFeignApi.decrease(orderFromDB.getProductId(), orderFromDB.getCount());
            //3.扣减账户余额
            log.info("------>订单微服务开始调用account账号，做扣减money");
            accountFeignApi.decrease(orderFromDB.getUserId(), orderFromDB.getMoney());
            log.info("------>订单微服务结束调用Account账号，做扣减完成");
            System.out.println();
            //4.修改订单状态
            //将订单状态从“零”修改为“一”
            log.info("------>修改订单状态");
            orderFromDB.setStatus(1);

            Example whereCondition = new Example(Order.class);
            Example.Criteria criteria = whereCondition.createCriteria();
            criteria.andEqualTo("userId", orderFromDB.getUserId());
            criteria.andEqualTo("status", 0);

            int updateResult = orderMapper.updateByExampleSelective(orderFromDB, whereCondition);
        }
    }
}