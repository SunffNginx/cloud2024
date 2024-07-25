package com.atguigu.cloud.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
    OpenFeign的相关配置类
*/
@Configuration
public class FeignConfig {
    //配置OpenFeign重试机制（1+2）
    @Bean
    public Retryer myRetryer(){
        //return Retryer.NEVER_RETRY;//Feign默认配置是不走重试机制的
        return Retryer.NEVER_RETRY;

        //最大重试机制次数为3（1+2），初始间隔时间为100ms，重试间最大间隔时间为1秒（1000ms）
//        return new Retryer.Default(100,1,3);

    }
    //配置OpenFeign日志打印级别
    @Bean
     public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

}
