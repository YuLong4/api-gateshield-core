# 1.SimpleChannelInboundHandler

`SimpleChannelInboundHandler` 是 Netty 中的一个类，它是 `ChannelInboundHandler` 接口的一个实现。这个类被设计用来处理入站事件，它能够简化处理入站消息的流程。

与普通的 `ChannelInboundHandler` 不同，`SimpleChannelInboundHandler` 在处理完消息后会自动释放资源，这是它的一个特性。它提供了一个 `channelRead0` 方法，用于处理接收到的每条消息，并且在处理完成后自动释放相关资源，避免了手动释放资源的麻烦。

这里是一个简单的示例，展示了如何使用 `SimpleChannelInboundHandler`：

```java
javaCopy code
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class YourHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        // 处理收到的消息
        System.out.println("Received: " + msg);

        // 不需要手动释放资源，SimpleChannelInboundHandler 会自动处理
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 处理异常情况
        cause.printStackTrace();
        ctx.close(); // 在出现异常时关闭通道
    }
}
```

这个示例中，`YourHandler` 类继承了 `SimpleChannelInboundHandler`，并重写了 `channelRead0` 方法来处理收到的消息。泛型参数 `<String>` 表示处理的消息类型为字符串，你可以根据实际情况替换成相应的消息类型。

在使用这个处理器的时候，确保正确处理异常情况，就像 `exceptionCaught` 方法中展示的那样。你可以根据需要记录异常日志、关闭通道或者执行其他适当的操作。





# 2.docker

1.Portioner

```
docker pull portainer/portainer Using default tag: latest

docker run -d --restart=always --name portainer -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock portainer/portainer
```

2.zookeeper

```
docker pull zookeeper:3.4.13

docker run --name my-zookeeper -p 2181:2181 -d zookeeper:3.4.13
添加自定义的配置文件zoo.cfg
docker run -d -p 2181:2181 --name my-zookeeper -v /Users/yulong/Desktop/BS/yyl/api-gateshield-core/docker/zookeeper/zoo.cfg:/conf/zoo.cfg zookeeper:3.4.13
```

# 解决升级到JDK17后cglib报错Unable to make protected final java.lang.Class java.lang.ClassLoader.defineClass()
启动报错Failed to create adaptive instance
```
运行程序的时候添加jvm参数
--add-opens java.base/java.lang=ALL-UNNAMED
```

## 开发日志
1月29日 2:00 
1、发现在pom中去掉zookeeper的依赖，就可以正常启动并且连接zookeeper
2、发现去掉zookeeper依赖后，使用dubbo直连方式测试成功，之前失败的原因是UserResDTO没有实现Serializable接口，但仍然出现
Unable to make field final int java.math.BigInteger.signum accessible: module java.base does not "opens java.math" to unnamed module @244038d0
报错
解决方式
使用jdk17编译运行dubbo 2.7.14项目
添加运行参数--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED --add-opens java.base/java.math=ALL-UNNAMED


1月29日 22:32重大发现
原本的自己的项目可以成功连接zookeeper、但是别人的项目打开了连接不上（卡在default scheme，然后超时），发现只要在修改运行配置中将java版本选择java 1.8，
即可成功运行并连接zookeeper，后续在解决如何修改项目默认为java 1.8。

至此 dubbo的服务提供者、消费者问题都已试验成功（后续将例子上传到github中）
zookeeper反复出现的连接问题也都得以解决

1月30日 14:56持续推进
只要将项目的sdk java版本修改为1.8，语言级别修改为8，在设置-java编译器中将项目的目标字节码版本也改为8，这样就能成功连接zookeeper
项目已经可以继续推进 TODO: 创建自己的dubbo服务提供者provider项目