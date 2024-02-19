package com.yyl.gateshield.test;

import com.yyl.gateshield.core.authorization.IAuth;
import com.yyl.gateshield.core.authorization.JwtUtil;
import com.yyl.gateshield.core.authorization.auth.AuthorizeService;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Shiro + Jwt
 */
public class ShiroTest {

    private final static Logger logger = LoggerFactory.getLogger(ShiroTest.class);

    @Test
    public void test_auth_service(){
        IAuth auth = new AuthorizeService();
        boolean validate = auth.validate("DPij8iUY", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJZdWxvbmciLCJleHAiOjE3MDg0MzgxMTUsImlhdCI6MTcwNzgzMzMxNSwia2V5IjoiWXVsb25nIn0.bFX_SDAvoO83vAOwjORuEKsJ2DRQ-my9_aoZYnZWXtg");
        logger.info(validate ? "验证成功" : "验证失败");
    }

    /**
     * 对指定字符串进行jwt编码和解码
     */
    @Test
    public void test_awt(){
        String issuer = "Yulong";
        long ttlMillis = 7 * 24 * 60 * 60 * 1000L;
        Map<String, Object> claims = new HashMap<>();
        claims.put("key", "Yulong");

        //编码
        String token = JwtUtil.encode(issuer, ttlMillis, claims);
        System.out.println("编码后: " + token);;

        //解码
        Claims parser = JwtUtil.decode(token);
        System.out.println("解码后: " + parser.getSubject());
    }

    @Test
    public void test_shiro(){
        // 1. 获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:test-shiro.ini");

        // 2. 得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("Yulong", "123");

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            System.out.println("身份验证失败");
        }

        logger.info(subject.isAuthenticated() ? "验证成功" : "验证失败");

        subject.logout();
    }

}
