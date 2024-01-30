package com.yyl.gateshield.bind;

import java.util.Map;

public interface IGenericReference {
    //$是函数名的一部分
    String $invoke(Map<String, Object> params);
}
