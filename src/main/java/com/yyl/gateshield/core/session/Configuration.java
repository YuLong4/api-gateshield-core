package com.yyl.gateshield.core.session;

import com.yyl.gateshield.core.authorization.IAuth;
import com.yyl.gateshield.core.authorization.auth.AuthorizeService;
import com.yyl.gateshield.core.bind.MapperRegistry;
import com.yyl.gateshield.core.bind.IGenericReference;
import com.yyl.gateshield.core.datasource.Connection;
import com.yyl.gateshield.core.executor.Executor;
import com.yyl.gateshield.core.executor.SimpleExecutor;
import com.yyl.gateshield.core.mapping.HttpStatement;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 会话生命周期配置项
 */
public class Configuration {

    private Logger logger = LoggerFactory.getLogger(Configuration.class);

    //网关 Netty 服务地址
    private String hostName = "127.0.0.1";

    //网关 Netty 服务端口
    private int port = 7397;

    // 网关 Netty 服务线程数配置
    private int bossNThreads = 1;
    private int workNThreads = 4;

    private final MapperRegistry mapperRegistry = new MapperRegistry(this);

    private final Map<String, HttpStatement> httpStatements = new HashMap<>();

    private final IAuth auth = new AuthorizeService();

    //RPC应用服务配置项 api-gateshield-test
    private final Map<String, ApplicationConfig> applicationConfigMap = new HashMap<>();
    //RPC注册中心配置项 zookeeper://127.0.0.1:2181
    private final Map<String, RegistryConfig> registryConfigMap = new HashMap<>();
    //RPC泛化服务配置项 com.yyl.gateshield.rpc.IActivityBooth
    private final Map<String, ReferenceConfig<GenericService>> referenceConfigMap = new HashMap<>();

    public Configuration(){
    }

    public synchronized void registryConfig(String applicationName, String address, String interfaceName, String version){
        if(applicationConfigMap.get(applicationName) == null) {
            ApplicationConfig application = new ApplicationConfig();
            application.setName(applicationName);
            application.setQosEnable(false);
            applicationConfigMap.put(applicationName, application);
        }

        if(registryConfigMap.get(applicationName) == null) {
            RegistryConfig registry = new RegistryConfig();
            registry.setAddress(address);
            registry.setRegister(false);
            registryConfigMap.put(applicationName, registry);
        }

        if(referenceConfigMap.get(interfaceName) == null) {
            ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
            reference.setInterface(interfaceName);
            reference.setVersion(version);
            reference.setGeneric("true");
            referenceConfigMap.put(interfaceName, reference);
        }
    }

    public ApplicationConfig getApplicationConfig(String applicationName){
        return applicationConfigMap.get(applicationName);
    }

    public RegistryConfig getRegistryConfig(String applicationName){
        return registryConfigMap.get(applicationName);
    }

    public ReferenceConfig<GenericService> getReferenceConfig(String interfaceName){
        return referenceConfigMap.get(interfaceName);
    }

    public void addMapper(HttpStatement httpStatement){
        mapperRegistry.addMapper(httpStatement);
    }

    public IGenericReference getMapper(String uri, GatewaySession gatewaySession){
        return mapperRegistry.getMapper(uri, gatewaySession);
    }

    public void addHttpStatement(HttpStatement httpStatement){
        httpStatements.put(httpStatement.getUri(), httpStatement);
    }

    public HttpStatement getHttpStatement(String uri){
        return httpStatements.get(uri);
    }

    public Executor newExecutor(Connection connection) {
        return new SimpleExecutor(this, connection);
    }

    public boolean authValidate(String uId, String token) {
        return auth.validate(uId, token);
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBossNThreads() {
        return bossNThreads;
    }

    public void setBossNThreads(int bossNThreads) {
        this.bossNThreads = bossNThreads;
    }

    public int getWorkNThreads() {
        return workNThreads;
    }

    public void setWorkNThreads(int workNThreads) {
        this.workNThreads = workNThreads;
    }
}
