package com.yyl.gateshield.core.executor;

import com.yyl.gateshield.core.mapping.HttpStatement;
import com.yyl.gateshield.core.executor.result.SessionResult;

import java.util.Map;

/**
 * 执行器
 */
public interface Executor {

    SessionResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception;

}
