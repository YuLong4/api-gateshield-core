package com.yyl.gateshield.session;

import com.yyl.gateshield.bind.IGenericReference;

public interface GatewaySession {
    Object get(String methodName, Object parameter);

    IGenericReference getMapper();

    Configuration getConfiguration();
}
