package demo.im.netty.learn02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyServer {
    public static void main(String[] args) {
        boot();
    }

    public static void boot() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                //服务端读取客户端数据，通过 childHandler()方法指定 服务端相关的数据处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new StringDecoder());
//                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
//                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//                                System.out.println(msg);
//                            }
//                        });

                        // 指定连接数据读写逻辑
                        // 给服务端添加一个逻辑处理器，来负责读取客户端来的数据
                        ch.pipeline().addLast(new FirstServerHandler());
                    }
                });
//                .bind(8000);
                bind(serverBootstrap, 8000);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("端口["+port+"]绑定成功");
                } else {
                    System.out.println("端口["+port+"]绑定失败");
                }
            }
        });
    }
}
