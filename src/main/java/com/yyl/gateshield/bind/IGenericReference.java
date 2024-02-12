package com.yyl.gateshield.bind;

import java.util.Map;

/**
 * 统一泛化调用接口
 */
public interface IGenericReference {
    //$是函数名的一部分
    Object $invoke(Map<String, Object> params);

}
