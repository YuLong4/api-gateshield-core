package com.yyl.gateshield.core.session;


/**
 * 网关会话工厂接口
 */
public interface GatewaySessionFactory {

    GatewaySession openSession(String uri);

}
