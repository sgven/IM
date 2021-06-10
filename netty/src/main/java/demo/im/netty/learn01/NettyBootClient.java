package demo.im.netty.learn01;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NettyBootClient {
    public static void main(String[] args) throws InterruptedException {
//        clientboot_demo1();
//        clientboot_demo2();
        clientboot_demo3();
    }

    public static void clientboot_demo1() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });

        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();

        while (true) {
            channel.writeAndFlush(new Date() + ":Hello World!");
            Thread.sleep(2000);
        }
    }

    public static void clientboot_demo2() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        bootstrap
                //1.指定线程模型
                .group(workGroup)
                //2.指定 IO模型 为NIO
                .channel(NioSocketChannel.class)
                //3.IO 处理逻辑
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });

        connect(bootstrap, "127.0.0.1", 8000);
    }

    public static void clientboot_demo3() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        bootstrap
                //1.指定线程模型
                .group(workGroup)
                //2.指定 IO模型 为NIO
                .channel(NioSocketChannel.class)
                //3.IO 处理逻辑
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });

        connect(bootstrap, "127.0.0.1", 8000, MAX_RETRY);
    }

    /**
     * 递归实现，失败重连
     * @param bootstrap
     * @param host
     * @param port
     */
    private static void connect(Bootstrap bootstrap, String host, int port) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else {
                System.err.println("连接失败，开始重连");
                //失败重连
                connect(bootstrap, host, port);
            }
        });
    }

    private static final int MAX_RETRY = 5;

    /**
     * 连接建立失败不会立即重新连接，而是通过一个指数退避的方式，比如1s,2s,4s,8s,以2的幂等来建立连接
     * @param bootstrap
     * @param host
     * @param port
     * @param retry
     */
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else if(retry == 0) {
                System.err.println("重试次数已用完，放弃连接!");
            } else {//失败重连
                //第几次重连
                int order = (MAX_RETRY - retry) + 1;
                //本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连...");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }
}
