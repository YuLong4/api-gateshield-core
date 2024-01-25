package com.yyl.gateshield.datasource;

import com.yyl.gateshield.session.Configuration;

public interface DataSourceFactory {

    DataSource getDataSource();

    void setProperties(Configuration configuration, String uri);
}
