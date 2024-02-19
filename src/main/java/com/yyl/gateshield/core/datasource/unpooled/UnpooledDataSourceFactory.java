package com.yyl.gateshield.core.datasource.unpooled;

import com.yyl.gateshield.core.datasource.DataSourceFactory;
import com.yyl.gateshield.core.datasource.DataSourceType;
import com.yyl.gateshield.core.session.Configuration;
import com.yyl.gateshield.core.datasource.DataSource;

public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected UnpooledDataSource dataSource;

    public UnpooledDataSourceFactory(){
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void setProperties(Configuration configuration, String uri) {
        dataSource.setConfiguration(configuration);
        dataSource.setDataSourceType(DataSourceType.Dubbo);
        dataSource.setHttpStatement(configuration.getHttpStatement(uri));
    }
}
