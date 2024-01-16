package com.yyl.gateshield.bind;

import com.yyl.gateshield.session.Configuration;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个 GenericReferenceRegistry 类是一个泛化调用注册中心，用于管理不同方法对应的 IGenericReference 实例。
 * 在 getGenericReference 方法中：
 * 通过传入的 methodName 从 knownGenericReferences 中获取相应的 GenericReferenceProxyFactory 实例。
 * 如果找不到对应 methodName 的 GenericReferenceProxyFactory，则抛出异常表示该类型未被注册。
 * 如果找到了相应的 GenericReferenceProxyFactory，则调用它的 newInstance 方法，该方法用于创建一个新的 IGenericReference 实例。
 * 最终返回创建的 IGenericReference 实例，这个实例可以执行对应方法的泛化调用。
 * 这个类的作用是提供一种中心化管理不同方法对应的泛化调用接口。通过在 knownGenericReferences 中注册不同的 GenericReferenceProxyFactory，可以动态地获取对应方法的 IGenericReference 实例，从而实现对不同方法的远程调用。
 */
public class GenericReferenceRegistry {

    private final Configuration configuration;

    //泛化调用静态代理工厂
    private final Map<String, GenericReferenceProxyFactory> knownGenericReferences = new HashMap<>();

    public GenericReferenceRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public IGenericReference getGenericReference(String methodName){
        GenericReferenceProxyFactory genericReferenceProxyFactory = knownGenericReferences.get(methodName);
        if (genericReferenceProxyFactory == null){
            throw new RuntimeException("Type " + methodName + " is not known to the GenericReferenceRegistry");
        }
        return genericReferenceProxyFactory.newInstance(methodName);
    }


    /*
        注册泛化调用服务接口方法
        通过参数传入的 application、interfaceName 和 methodName，获取了一些 Dubbo 配置，
        比如 ApplicationConfig、RegistryConfig 和 ReferenceConfig<GenericService>。这些配置用于构建 Dubbo 服务。
        使用获取到的 Dubbo 配置，通过 DubboBootstrap 的实例进行服务的启动和配置。
        从 ReferenceConfigCache 中获取了一个泛化调用的 GenericService。
        创建了一个 GenericReferenceProxyFactory，并将获取到的 GenericService 传递给它。
        最后将这个 GenericReferenceProxyFactory 保存在 knownGenericReferences 中，以便后续使用。
        目的是将泛化调用的服务注册到一个中心化的地方，方便之后通过 methodName 来获取泛化调用的代理工厂。用于管理 Dubbo 服务的注册和泛化调用的接口。
     */
    public void addGenericReference(String application, String interfaceName, String methodName){
        //获取基础服务
        ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
        RegistryConfig registryConfig = configuration.getRegistryConfig(application);
        ReferenceConfig<GenericService> referenceConfig = configuration.getReferenceConfig(interfaceName);
        //构建Dubbo服务
        DubboBootstrap dubboBootstrap = DubboBootstrap.getInstance();
        dubboBootstrap
                .application(applicationConfig)
                .registry(registryConfig)
                .reference(referenceConfig)
                .start();

        //获取泛化调用服务
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(referenceConfig);
        //创建并保存泛化工厂
        knownGenericReferences.put(methodName, new GenericReferenceProxyFactory(genericService));
    }
}
