package com.yyl.gateshield.socket.agreement;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 请求解析器
 */
public class RequestParser {

    private final FullHttpRequest request;

    public RequestParser(FullHttpRequest request) {
        this.request = request;
    }

    public Map<String, Object> parse(){
        //获取contentType
        String contentType = getContentType();

        //获取请求类型
        HttpMethod method = request.method();

        if (method == HttpMethod.GET){
            Map<String, Object> parameterMap = new HashMap<>();
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().forEach(
                    (key, value) -> parameterMap.put(key, value.get(0))
            );
            return parameterMap;
        } else if (method == HttpMethod.POST){
            switch (contentType){
                case "multipart/form-data":
                    Map<String, Object> parameterMap = new HashMap<>();
                    HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
                    decoder.offer(request);
                    decoder.getBodyHttpDatas().forEach(
                            data -> {
                                Attribute attribute = (Attribute) data;
                                try {
                                    parameterMap.put(data.getName(), attribute.getValue());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                    return parameterMap;

                case "application/json":
                    ByteBuf byteBuf = request.content().copy();
                    if(byteBuf.isReadable()){
                        String content = byteBuf.toString(StandardCharsets.UTF_8);
                        return JSON.parseObject(content);
                    }
                    break;
                default:
                    throw new RuntimeException("未实现的协议类型 Content-Type：" + contentType);
            }
        }
        throw new RuntimeException("未实现的请求类型 HttpMethod：" + method);
    }

    private String getContentType(){
        Optional<Map.Entry<String, String>> header = request.headers().entries().stream().filter(
                val -> val.getKey().equals("Content-Type")
        ).findAny();

        Map.Entry<String, String> entry = header.orElse(null);
        assert entry != null;

        // application/json、multipart/form-data;
        String contentType = entry.getValue();
        int idx = contentType.indexOf(";");

        if (idx > 0){
            return contentType.substring(0, idx);
        } else {
            return contentType;
        }
    }

}
