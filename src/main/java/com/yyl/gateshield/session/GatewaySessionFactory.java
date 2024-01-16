package com.yyl.gateshield.session;

import io.netty.channel.Channel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface GatewaySessionFactory {

    GatewaySession openSession();

}
