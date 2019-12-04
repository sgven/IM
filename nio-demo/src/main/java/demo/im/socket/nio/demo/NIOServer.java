package demo.im.socket.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws IOException{
        Selector serverSelector = Selector.open();
        Selector clientSelector = Selector.open();

        new Thread(() -> {
            try {
                // 对应IO编程模型中服务端启动
                ServerSocketChannel listenerChannel = ServerSocketChannel.open();// ServerSocketChannel 相当于IO模型中的 ServerSocket
                listenerChannel.socket().bind(new InetSocketAddress(8000));
                // 切换异步非阻塞
                listenerChannel.configureBlocking(false);
                // ServerSocketChannel 注册到 serverSelector
                listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                while (true) {
                    // 监测是否有新的连接，这里的1指的是阻塞的时间为 1ms
                    if (serverSelector.select(1) > 0) {
                        Set<SelectionKey> set = serverSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = set.iterator();

                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();

                            if (key.isAcceptable()) {
                                try {
                                    // (1).每来一个新连接，不需要创建一个线程，而是直接注册到clientSelector
                                    SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();// SocketChannel 相当于IO模型中的 Socket
                                    // 切换异步非阻塞
                                    clientChannel.configureBlocking(false);
                                    // 将客户端连接 SocketChannel 注册到 clientSelector
                                    clientChannel.register(clientSelector, SelectionKey.OP_READ);
                                } finally {
                                    keyIterator.remove();
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    // (2).批量轮询是否有哪些连接有数据可读，这里的1指的是阻塞的时间为 1ms
                    if (clientSelector.select(1) > 0) {
                        Set<SelectionKey> set = clientSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = set.iterator();

                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();

                            if (key.isReadable()) {
                                try {
                                    SocketChannel clientChannel = (SocketChannel) key.channel();
                                    // 指定缓冲区大小
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    // (3).面向 Buffer
                                    clientChannel.read(byteBuffer);
                                    // 切换到读取模式
                                    byteBuffer.flip();
                                    System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer)
                                            .toString());
                                } finally {
                                    keyIterator.remove();
                                    key.interestOps(SelectionKey.OP_READ);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {

            }
        }).start();
    }
}
