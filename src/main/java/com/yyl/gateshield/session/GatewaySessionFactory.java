package com.yyl.gateshield.session;


public interface GatewaySessionFactory {

    GatewaySession openSession(String uri);

}
