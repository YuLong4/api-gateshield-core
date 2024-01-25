package com.yyl.gateshield.bind;


import com.yyl.gateshield.session.GatewaySession;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

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

/**
 * 映射代理调用
 */
public class MapperProxy implements MethodInterceptor {

    private final GatewaySession gatewaySession;
    private final String uri;

    public MapperProxy(GatewaySession gatewaySession, String uri) {
        this.gatewaySession = gatewaySession;
        this.uri = uri;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        MapperMethod linkMethod = new MapperMethod(uri, method, gatewaySession.getConfiguration());
        // 暂时只获取第0个参数
        return linkMethod.execute(gatewaySession, args[0]);
    }
}
