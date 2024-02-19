package com.yyl.gateshield.core.datasource;

/**
 * 数据源接口，RPC、HTTP都当作连接的数据资源使用
 */
public interface DataSource {
    Connection getConnection();
}
