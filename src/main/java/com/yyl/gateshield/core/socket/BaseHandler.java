package com.yyl.gateshield.core.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 数据处理器基类
 * 这段代码定义了一个抽象类 BaseHandler<T>，它是一个泛型类，继承自 SimpleChannelInboundHandler<T>。它的目的是为了处理收到的消息，并将处理的逻辑交给子类来实现。
 * 在这个抽象类中，重写了 channelRead0 方法，当有消息到达时会调用这个方法。channelRead0 方法内部调用了 session 方法，并传递了 ChannelHandlerContext、Channel 对象以及收到的消息 T。
 * 这个抽象类中的 session 方法是抽象的，因此需要子类来实现。子类需要提供 session 方法的具体实现来处理收到的消息 T。通过这种方式，BaseHandler 提供了一个模板方法模式，让子类实现特定的消息处理逻辑。
 */
public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
        session(ctx, ctx.channel(), msg);
    }

    protected abstract void session(ChannelHandlerContext ctx, final Channel channel, T request);
}
