package demo.im.system.access;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;


public class MyChannelHandlerPool {

    public MyChannelHandlerPool(){}

    /**
     * ChannelGroup连接通道池，其中维护了多个Channel连接
     * 注意：我们通过 Channel内部的 AttributeKey， 来缓存用户信息
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 在线用户map缓存，用于快速判断用户是否在线
     * key=user,value=channel
     *
     * why: 还是需要这个map的，可以快速判断用户是否在线
     */
    public static final ConcurrentHashMap<String, Channel> userChanneMap = new ConcurrentHashMap<>();

}
