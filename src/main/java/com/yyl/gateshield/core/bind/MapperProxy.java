package com.yyl.gateshield.core.bind;


import com.yyl.gateshield.core.session.GatewaySession;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * 映射代理调用
 * CGLib
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
        return linkMethod.execute(gatewaySession, (Map<String, Object>) args[0]);
    }
}
