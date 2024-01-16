package com.yyl.gateshield.test;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;

public class RPCTest {

    @Test
    public void test_rpc(){
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("api-gateshield-test");
        applicationConfig.setQosEnable(false);

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        registryConfig.setRegister(false);

        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
//        referenceConfig.setInterface("com.yyl.gateshield.rpc.IActivityBooth");
        referenceConfig.setInterface("com.yyl.gateshield.rpc.IActivityBooth");
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setGeneric("true");

        //构建Dubbo服务
        DubboBootstrap dubboBootstrap = DubboBootstrap.getInstance();
        dubboBootstrap
                .application(applicationConfig)
                .registry(registryConfig)
                .reference(referenceConfig)
                .start();

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(referenceConfig);

        Object result = genericService.$invoke("sayHi", new String[]{"java.lang.String"}, new Object[]{"world"});
        System.out.println(result);
    }

}
