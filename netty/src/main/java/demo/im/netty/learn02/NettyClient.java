package demo.im.netty.learn02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) {
        clientboot();
    }

    public static void clientboot() {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel ch) throws Exception {
//                        ch.pipeline().addLast(new StringEncoder());

                        //指定连接数据读写逻辑
                        //ch.pipeline() 返回的是和这条连接相关的逻辑处理链，采用了责任链模式
                        //addLast()  添加一个逻辑处理器，它是干嘛的呢？就是在客户端建立连接成功之后，向服务端写数据
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
//                }).connect("127.0.0.1", 8000);
                });
        connect(bootstrap, "127.0.0.1", 8000);

        /*Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();

        while (true) {
            channel.writeAndFlush(new Date() + ":Hello World!");
            Thread.sleep(2000);
        }*/
    }

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
}
