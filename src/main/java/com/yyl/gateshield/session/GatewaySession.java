package com.yyl.gateshield.session;

import com.yyl.gateshield.bind.IGenericReference;

public interface GatewaySession {
    Object get(String uri, Object parameter);

    IGenericReference getMapper(String uri);

    Configuration getConfiguration();
}
