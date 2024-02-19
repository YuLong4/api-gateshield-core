package com.yyl.gateshield.core.session.defaults;

import com.yyl.gateshield.core.datasource.DataSource;
import com.yyl.gateshield.core.datasource.DataSourceFactory;
import com.yyl.gateshield.core.datasource.unpooled.UnpooledDataSourceFactory;
import com.yyl.gateshield.core.executor.Executor;
import com.yyl.gateshield.core.session.Configuration;
import com.yyl.gateshield.core.session.GatewaySessionFactory;
import com.yyl.gateshield.core.session.GatewaySession;

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

        // 创建执行器
        Executor executor = configuration.newExecutor(dataSource.getConnection());
        // 创建会话: DefaultGatewaySession
        return new DefaultGatewaySession(configuration, uri, executor);
    }
}
