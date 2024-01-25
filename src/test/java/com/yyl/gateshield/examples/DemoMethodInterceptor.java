package com.yyl.gateshield.examples;

import com.yyl.gateshield.test.CGLibTest;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class DemoMethodInterceptor implements MethodInterceptor {

    private final Logger logger = LoggerFactory.getLogger(DemoMethodInterceptor.class);

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        logger.info("------Before selling fruits------");
        // 执行代理的目标对象的方法
        Object result = proxy.invokeSuper(obj, args);
        logger.info("------After selling fruits------");
        return result;
    }
}
