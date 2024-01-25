package com.yyl.gateshield.test;

import com.yyl.gateshield.examples.DemoMethodInterceptor;
import com.yyl.gateshield.examples.FruitGrower;
import com.yyl.gateshield.examples.Sales;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 《深入理解RPC框架原理与实现》
 *  P184  CGLIB例子
 */
public class CGLibTest {
    private final Logger logger = LoggerFactory.getLogger(CGLibTest.class);

    @Test
    public void test_CGLib(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(FruitGrower.class);
        enhancer.setCallback(new DemoMethodInterceptor());
        Sales proxy = (Sales) enhancer.create();
        proxy.sellFruits();
    }
}
