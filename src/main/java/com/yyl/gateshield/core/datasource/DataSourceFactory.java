package com.yyl.gateshield.core.datasource;

import com.yyl.gateshield.core.session.Configuration;

public interface DataSourceFactory {

    DataSource getDataSource();

    void setProperties(Configuration configuration, String uri);
}
