package com.yyl.gateshield.bind;


import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;

/**
 * 目的是作为一个代理类，用于代理调用 RPC（远程过程调用）的泛化服务。
 * 该类的构造函数接收一个 GenericService 实例和一个方法名 methodName。
 * 在 intercept 方法中，通过 MethodInterceptor 接口实现的拦截逻辑，捕获了被代理对象方法的调用。
 * 提取了方法的参数类型，并将其转换为参数类型名称的字符串数组 parameters。
 * 使用 genericService.$invoke 方法执行了泛化调用，传递了方法名 methodName、参数类型数组 parameters 和实际参数 objects。
 * 最终，返回了泛化调用的结果。
 * 这个代理类的作用是将实际方法调用转换为泛化调用，以便于通过 genericService.$invoke 方法实现对远程服务的泛化调用。
 */
public class GenericReferenceProxy implements MethodInterceptor {

    /**
     * RPC泛化调用服务
     */
    private final GenericService genericService;

    /**
     * RPC泛化调用方法
     */
    private final String methodName;

    public GenericReferenceProxy(GenericService genericService, String methodName) {
        this.genericService = genericService;
        this.methodName = methodName;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameters = new String[parameterTypes.length];

        for(int i = 0; i < parameters.length; i++){
            parameters[i] = parameterTypes[i].getName();
        }

        // 举例：genericService.$invoke("sayHi", new String[]{"java.lang.String"}, new Object[]{"world"});
        return genericService.$invoke(methodName, parameters, objects);
    }
}
