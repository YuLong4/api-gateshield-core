package com.yyl.gateshield.test;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;

public class RPCTest2 {
    @Test
    void test_RPC(){
        // 引用远程服务
        // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();

        // 弱类型接口名
//        reference.setInterface("com.yyl.gateshield.rpc.IActivityBooth");
//        reference.setVersion("1.0.0");
//        reference.setTimeout(30000);
//        // 声明为泛化接口
//        reference.setGeneric(true);



        // 用org.apache.dubbo.rpc.service.GenericService可以替代所有接口引用
//        GenericService genericService = reference.get();

        // 基本类型以及Date,List,Map等不需要转换，直接调用
//        Object result = genericService.$invoke("sayHello", new String[] {"java.lang.String"}, new Object[] {"world"});

//        System.out.println(result);
    }
}
