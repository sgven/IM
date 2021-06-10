package demo.im.netty.learn04.serialize.impl;


import com.alibaba.fastjson.JSON;
import demo.im.netty.learn04.serialize.Serializer;
import demo.im.netty.learn04.serialize.SerializerAlgorithm;

public class JSONSerializer implements Serializer {

    public byte getSerializerAlgorithm() {

        return SerializerAlgorithm.JSON;
    }

    public byte[] serialize(Object object) {

        return JSON.toJSONBytes(object);
    }

    public <T> T deserialize(Class<T> clazz, byte[] bytes) {

        return JSON.parseObject(bytes, clazz);
    }
}
