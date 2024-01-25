package com.yyl.gateshield.datasource.unpooled;

import com.yyl.gateshield.datasource.DataSource;
import com.yyl.gateshield.datasource.DataSourceFactory;
import com.yyl.gateshield.datasource.DataSourceType;
import com.yyl.gateshield.session.Configuration;

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
