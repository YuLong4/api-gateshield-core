package com.yyl.gateshield.session.defaults;

import com.yyl.gateshield.datasource.DataSource;
import com.yyl.gateshield.datasource.DataSourceFactory;
import com.yyl.gateshield.datasource.unpooled.UnpooledDataSourceFactory;
import com.yyl.gateshield.session.Configuration;
import com.yyl.gateshield.session.GatewaySession;
import com.yyl.gateshield.session.GatewaySessionFactory;

/**
 * 默认网关会话工厂
 */
public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession(String uri) {
        // 获取数据源连接信息， 把Dubbo、HTTP抽象为一种连接资源
        DataSourceFactory dataSourceFactory = new UnpooledDataSourceFactory();
        dataSourceFactory.setProperties(configuration, uri);
        DataSource dataSource = dataSourceFactory.getDataSource();

        return new DefaultGatewaySession(configuration, uri, dataSource);
    }
}
