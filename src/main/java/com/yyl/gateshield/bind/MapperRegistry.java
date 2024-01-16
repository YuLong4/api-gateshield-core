package com.yyl.gateshield.bind;

import com.yyl.gateshield.mapping.HttpStatement;
import com.yyl.gateshield.session.Configuration;
import com.yyl.gateshield.session.GatewaySession;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 这个 GenericReferenceRegistry 类是一个泛化调用注册中心，用于管理不同方法对应的 IGenericReference 实例。
 * 在 getGenericReference 方法中：
 * 通过传入的 methodName 从 knownGenericReferences 中获取相应的 GenericReferenceProxyFactory 实例。
 * 如果找不到对应 methodName 的 GenericReferenceProxyFactory，则抛出异常表示该类型未被注册。
 * 如果找到了相应的 GenericReferenceProxyFactory，则调用它的 newInstance 方法，该方法用于创建一个新的 IGenericReference 实例。
 * 最终返回创建的 IGenericReference 实例，这个实例可以执行对应方法的泛化调用。
 * 这个类的作用是提供一种中心化管理不同方法对应的泛化调用接口。通过在 knownGenericReferences 中注册不同的 GenericReferenceProxyFactory，可以动态地获取对应方法的 IGenericReference 实例，从而实现对不同方法的远程调用。
 */
public class MapperRegistry {

    private final Configuration configuration;

    //泛化调用静态代理工厂
    private final Map<String, MapperProxyFactory> knownMappers = new HashMap<>();

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public IGenericReference getMapper(String uri, GatewaySession gatewaySession) {
        final MapperProxyFactory mapperProxyFactory = knownMappers.get(uri);
        if (mapperProxyFactory == null){
            throw new RuntimeException("Uri " + uri + " is not known to the MapperRegistry");
        }
        try {
            return mapperProxyFactory.newInstance(gatewaySession);
        } catch (Exception e){
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public void addMapper(HttpStatement httpStatement){
        String uri = httpStatement.getUri();
        // 如果重复注册则报错
        if (hasMapper(uri)){
            throw new RuntimeException("Uri " + uri + " is already known to the MapperRegistry");
        }
        knownMappers.put(uri, new MapperProxyFactory(uri));
        // 保存接口映射信息
        configuration.addHttpStatement(httpStatement);
    }

    public <T> boolean hasMapper(String uri){
        return knownMappers.containsKey(uri);
    }
}
