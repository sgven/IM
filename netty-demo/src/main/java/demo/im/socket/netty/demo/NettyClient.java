package demo.im.socket.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Date;

public class NettyClient {

    public static void main(String[] args) throws Exception {
//        bootClientSimpleDemo();
        NettyClient client = new NettyClient("127.0.0.1", 8000);
        client.start();
        Channel channel = client.getChannel();
        while (true) {
//            channel.writeAndFlush(new Date() + ": hello world netty");
            channel.writeAndFlush(new Date() + ": hello world!");
            // TODO: 把isWritable变成true
            System.out.println("write"+ channel.isWritable());
            channel.flush();
            Thread.sleep(2000);
        }
    }

    private final String host;
    private final int port;
    private Channel channel;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Channel getChannel() {
        return channel;
    }

    private void start() throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3) 使用NioSocketChannel来作为连接用的channel类
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new ClientHandler()); // 客户端处理类
                }
            });

            // Start the client.发起异步连接请求，绑定连接端口和host信息
            ChannelFuture f = b.connect(this.host, this.port).sync(); // (5)
            // Wait until the connection is closed.
//            f.channel().closeFuture().sync();

            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture arg0) throws Exception {
                    if (f.isSuccess()) {
                        System.out.println("连接服务器成功");
                    } else {
                        System.out.println("连接服务器失败");
                        f.cause().printStackTrace();
                        workerGroup.shutdownGracefully(); //关闭线程组
                    }
                }
            });
            this.channel = f.channel();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static void bootClientSimpleDemo() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new StringDecoder());
                    }
                });
        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();
        while (true) {
            channel.writeAndFlush(new Date() + ": hello world netty");
            Thread.sleep(2000);
        }
    }
}
