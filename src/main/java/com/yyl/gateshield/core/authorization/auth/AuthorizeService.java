package com.yyl.gateshield.core.authorization.auth;

import com.yyl.gateshield.core.authorization.GatewayAuthorizingToken;
import com.yyl.gateshield.core.authorization.IAuth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * 认证服务实现
 */
public class AuthorizeService implements IAuth {

    private Subject subject;

    public AuthorizeService(){
        //1. 获取SecurityManager工厂 此处使用shiro.ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //2. 得到 SecurityManager 实例 并绑定给 SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3. 得到Subject及Token
        this.subject = SecurityUtils.getSubject();
    }

    @Override
    public boolean validate(String id, String token) {
        try{
            //身份验证
            subject.login(new GatewayAuthorizingToken(id, token));
            return subject.isAuthenticated();
        } finally {
            // 退出
            subject.logout();
        }
    }
}
