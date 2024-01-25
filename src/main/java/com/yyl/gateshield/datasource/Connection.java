package com.yyl.gateshield.datasource;

/**
 * 连接接口
 */
public interface Connection {
    Object execute(String method, String[] parametersTypes, String[] parameterNames, Object[] args);
}
