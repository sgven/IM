package demo.im.system;

import demo.im.system.access.NettyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleIMApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SimpleIMApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		bootNettyServer();
	}

	/**
	 * ApplicationInitListener 监听到系统启动时，启动IM服务
	 * 此时可从（容器Spring）上下文获取需要的资源、配置信息，并通过构造注入 or setter注入需要的类
	 * 比如端口号、是否启用netty服务
	 * 	netty.port
	 * 	netty.init
	 * 比如注入redis、dao，redis简单做用户验证，dao做消息持久化更新（更新消息状态，比如是否已发送）
	 *
	 * 这里只做简单演示，就不写复杂了
	 */
	private void bootNettyServer() throws Exception {
		//启动netty
		new NettyServer(12345).start();
	}
}
