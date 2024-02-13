package com.yyl.gateshield.executor;

import com.yyl.gateshield.executor.result.SessionResult;
import com.yyl.gateshield.mapping.HttpStatement;

import java.util.Map;

/**
 * 执行器
 */
public interface Executor {

    SessionResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception;

}
