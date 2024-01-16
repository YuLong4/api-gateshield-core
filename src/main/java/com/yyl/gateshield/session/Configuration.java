package com.yyl.gateshield.session;

import com.yyl.gateshield.bind.GenericReferenceRegistry;
import com.yyl.gateshield.bind.IGenericReference;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Configuration {

    private final GenericReferenceRegistry registry = new GenericReferenceRegistry(this);
    //RPC应用服务配置项 api-gateshield-test
    private final Map<String, ApplicationConfig> applicationConfigMap = new HashMap<>();
    //RPC注册中心配置项 zookeeper://127.0.0.1:2181
    private final Map<String, RegistryConfig> registryConfigMap = new HashMap<>();
    //RPC泛化服务配置项 com.yyl.gateshield.rpc.IActivityBooth
    private final Map<String, ReferenceConfig<GenericService>> referenceConfigMap = new HashMap<>();

    public Configuration(){
        //TODO 后期从配置中获取
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("api-gateshield-test");
        applicationConfig.setQosEnable(false);

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        registryConfig.setRegister(false);

        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface("com.yyl.gateshield.rpc.IActivityBooth");
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setGeneric("true");

        applicationConfigMap.put("api-gateshield-test", applicationConfig);
        registryConfigMap.put("api-gateshield-test", registryConfig);
        referenceConfigMap.put("com.yyl.gateshield.rpc.IActivityBooth", referenceConfig);
    }

    public ApplicationConfig getApplicationConfig(String applicationName){
        return applicationConfigMap.get(applicationName);
    }

    public RegistryConfig getRegistryConfig(String applicationName){
        return registryConfigMap.get(applicationName);
    }

    public ReferenceConfig<GenericService> getReferenceConfig(String interfaceName){
        return referenceConfigMap.get(interfaceName);
    }

    public void addGenericReference(String application, String interfaceName, String methodName){
        registry.addGenericReference(application, interfaceName, methodName);
    }

    public IGenericReference getGenericReference(String methodName){
        return registry.getGenericReference(methodName);
    }
}
