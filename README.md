# api-gateshield-core
### 网关的核心系统：用于网络通信转换处理，承接HTTP请求，调用RPC服务，责任链模块调用。

# docker相关

1.Portioner安装

```
docker pull portainer/portainer Using default tag: latest

docker run -d --restart=always --name portainer -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock portainer/portainer
```

2.zookeeper推荐版本与安装

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

1月31日 02:06自己的provider测试项目出现问题，收到请求后，调试时出现字节码与代码不匹配错误，同时调试时发现抛出异常

2月13日 01:03将代码同步至第六章，目前可以顺利地使用自己的测试程序和网关程序进行调用了