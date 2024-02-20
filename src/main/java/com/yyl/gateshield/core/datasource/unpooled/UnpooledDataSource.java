package com.yyl.gateshield.core.datasource.unpooled;

import com.yyl.gateshield.core.datasource.DataSourceType;
import com.yyl.gateshield.core.datasource.connection.DubboConnection;
import com.yyl.gateshield.core.session.Configuration;
import com.yyl.gateshield.core.datasource.Connection;
import com.yyl.gateshield.core.datasource.DataSource;
import com.yyl.gateshield.core.mapping.HttpStatement;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UnpooledDataSource implements DataSource {

    private Logger logger = LoggerFactory.getLogger(UnpooledDataSource.class);

    private Configuration configuration;
    private HttpStatement httpStatement;
    private DataSourceType dataSourceType;

    @Override
    public Connection getConnection() {
        switch (dataSourceType){
            case HTTP:
                // TODO 预留接口
                break;
            case Dubbo:
                logger.info("case: Dubbo, 目标为Dubbo服务");
                //配置信息
                String application = httpStatement.getApplication();
                String interfaceName = httpStatement.getInterfaceName();
                //获取服务
                ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
                RegistryConfig registryConfig = configuration.getRegistryConfig(application);
                ReferenceConfig<GenericService> refence = configuration.getReferenceConfig(interfaceName);
                return new DubboConnection(applicationConfig, registryConfig, refence);
            default:
                break;
        }
        throw new RuntimeException("DataSourceType：" + dataSourceType + "没有对应的数据源实现");
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setHttpStatement(HttpStatement httpStatement) {
        this.httpStatement = httpStatement;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }
}
