package com.yyl.gateshield.session.defaults;

import com.yyl.gateshield.bind.IGenericReference;
import com.yyl.gateshield.datasource.Connection;
import com.yyl.gateshield.datasource.DataSource;
import com.yyl.gateshield.executor.Executor;
import com.yyl.gateshield.mapping.HttpStatement;
import com.yyl.gateshield.session.Configuration;
import com.yyl.gateshield.session.GatewaySession;
import com.yyl.gateshield.type.SimpleTypeRegistry;

import java.util.Map;

/**
 * 默认网关会话实现类
 */
public class DefaultGatewaySession implements GatewaySession {

    private Configuration configuration;
    private String uri;
    private Executor executor;

    public DefaultGatewaySession(Configuration configuration, String uri, Executor executor){
        this.configuration = configuration;
        this.uri = uri;
        this.executor = executor;
    }

    @Override
    public Object get(String methodName, Map<String, Object> params) {
        HttpStatement httpStatement = configuration.getHttpStatement(uri);
        try{
            return executor.exec(httpStatement, params);
        } catch (Exception e){
            throw new RuntimeException("Error exec get. Cause: " + e);
        }
   }

    @Override
    public Object post(String methodName, Map<String, Object> params) {
        return get(methodName, params);
    }

    @Override
    public IGenericReference getMapper() {
        return configuration.getMapper(uri, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
