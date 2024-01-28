package com.yyl.gateshield.bind;

import com.yyl.gateshield.mapping.HttpCommandType;
import com.yyl.gateshield.session.Configuration;
import com.yyl.gateshield.session.GatewaySession;

import java.lang.reflect.Method;

/**
 * 绑定调用方法
 */
public class MapperMethod {

    private String methodName;
    private final HttpCommandType command;

    public MapperMethod(String uri, Method method, Configuration configuration) {
        this.methodName = configuration.getHttpStatement(uri).getMethodName();
        this.command = configuration.getHttpStatement(uri).getHttpCommandType();
    }

    public Object execute(GatewaySession session, Object args){
        Object result = null;
        switch (command) {
            case GET:
                result = session.get(methodName, args);
                break;
            case POST:
                break;
            case PUT:
                break;
            case DELETE:
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + command);
        }
        return result;
    }
}