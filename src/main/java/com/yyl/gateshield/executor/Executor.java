package com.yyl.gateshield.executor;

import com.yyl.gateshield.executor.result.GatewayResult;
import com.yyl.gateshield.mapping.HttpStatement;

import java.util.Map;

/**
 * 执行器
 */
public interface Executor {

    GatewayResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception;

}
