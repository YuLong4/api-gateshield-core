package com.yyl.gateshield.session.defaults;

import com.yyl.gateshield.session.Configuration;
import com.yyl.gateshield.session.GatewaySession;
import com.yyl.gateshield.session.GatewaySessionFactory;

public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession() {
        return new DefaultGatewaySession(configuration);
    }
}
