package com.yyl.gateshield.core.bind;

import com.yyl.gateshield.core.executor.result.SessionResult;

import java.util.Map;

/**
 * 统一泛化调用接口
 */
public interface IGenericReference {
    //$是函数名的一部分
    SessionResult $invoke(Map<String, Object> params);

}
