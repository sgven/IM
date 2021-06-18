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
//		initNettyContextBean();//初始化netty服务需要的上下文bean
//		initNoRead();//初始化未读数
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



//	private void initNettyContextBean() {
//		this.dao = SpringContextHolder.getBean("operateDao");
//		this.iRedisProxy = SpringContextHolder.getBean("shardedRedisProxyService");
//	}
//
//	/**
//	 * 初始化消息未读数
//	 */
//	private void initNoRead() {
////        from IMMsgRelation where isDelete=0 and isRead=0
//		List<IMMsgRelation> relationList = dao.find(IMMsgRelation.class, new String[] {"isDelete", "isRead"}, new Object[] {0, 0});
//		if (relationList != null && relationList.size() > 0) {
//			//未读消息，按接收者分组
//			Map<String, List<IMMsgRelation>> userGroupMap = relationList.stream().collect(Collectors.groupingBy(e -> e.getRecipientId()));
//			for (Map.Entry<String, List<IMMsgRelation>> entry : userGroupMap.entrySet()) {
//				String user = entry.getKey();
//				int noReadTotalCount = entry.getValue().size();
//				iRedisProxy.hset(REDIS_KEY_PREFIX_USER_MSG+user, "noReadTotalCount", noReadTotalCount+"");
//			}
//		}
//	}
//
//	/**
//	 * 启动IM接入层服务
//	 * @throws Exception
//	 */
//	private void bootNettyServer() throws Exception {
//		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();
//		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
//		int port = 12345;//默认端口
//		String enable = "yes";//是否启用netty服务
//		if (beanFactory.containsBean("nettyConfig")) {
//			this.nettyConfig = SpringContextHolder.getBean("nettyConfig");
//			port = nettyConfig.getPort();
//			enable = nettyConfig.getEnable();
//			if ("yes".equals(enable)) {
//				new NettyServer(port, iRedisProxy, dao).start();
//			}
//		}
//	}
}
