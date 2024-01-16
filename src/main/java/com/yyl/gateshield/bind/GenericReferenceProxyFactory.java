package com.yyl.gateshield.bind;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.dubbo.rpc.service.GenericService;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这个 GenericReferenceProxyFactory 类是一个工厂类，用于创建 IGenericReference 的实例。
 * 在 newInstance 方法中：
 * 使用 computeIfAbsent 方法从 genericReferenceCache 缓存中获取指定 method 的 IGenericReference 实例。如果缓存中不存在对应的实例，则进行创建。
 * 在创建新的 IGenericReference 实例时，首先创建了一个 GenericReferenceProxy 实例，该实例负责实际的泛化调用。
 * 使用 InterfaceMaker 类创建了一个接口。该接口与泛化调用的方法对应，这种方式可以动态地创建一个新的接口，该接口包含指定方法签名的定义。
 * 使用 Enhancer 类创建一个代理对象。这个代理对象继承了 Object 类并实现了 IGenericReference 接口以及动态生成的接口。
 * 将 GenericReferenceProxy 设置为代理对象的回调方法，这样在调用代理对象的方法时会执行 GenericReferenceProxy 的拦截逻辑，实现对泛化调用的处理。
 * 返回最终创建的代理对象作为 IGenericReference 实例。
 * 总的来说，GenericReferenceProxyFactory 类的作用是根据传入的方法名动态地创建一个实现了 IGenericReference 接口的代理对象，
 * 该代理对象可以执行泛化调用并与远程服务进行通信。这种方式可以在运行时动态地创建接口和代理对象，从而实现对远程服务的灵活调用。
 */
public class GenericReferenceProxyFactory {

    private final GenericService genericService;

    private final Map<String, IGenericReference> genericReferenceCache = new ConcurrentHashMap<>();

    public GenericReferenceProxyFactory(GenericService genericService) {
        this.genericService = genericService;
    }

    public IGenericReference newInstance(String method){
        return genericReferenceCache.computeIfAbsent(method, k -> {
            //泛化调用
            GenericReferenceProxy genericReferenceProxy = new GenericReferenceProxy(genericService, method);

            //创建接口
            InterfaceMaker interfaceMaker = new InterfaceMaker();
            interfaceMaker.add(new Signature(method, Type.getType(String.class), new Type[]{Type.getType(String.class)}), null);
            Class<?> interfaceClass = interfaceMaker.create();

            //代理对象
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(new Class[]{IGenericReference.class, interfaceClass});
            enhancer.setCallback(genericReferenceProxy);
            return (IGenericReference) enhancer.create();
        });
    }
}
