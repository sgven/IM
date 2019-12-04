package demo.im.socket.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

// 可以直接用IOClient与NIOServer通信
public class NIOClient {

    public static void main(String[] args) throws Exception {
        try {
            //1、创建socker通道
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));
            //2、切换异步非阻塞
            socketChannel.configureBlocking(false); //1.7及以上
            while (true) {
                //3、指定缓冲区大小
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                byteBuffer.put((new Date() + ": hello world nio").getBytes());
                //4、切换到读取模式
                byteBuffer.flip();
                //5、写入到缓冲区
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
                Thread.sleep(2000);
            }
        } catch (IOException e) {
        } finally {
            //6、关闭通道
//            socketChannel.close();
        }
    }
}
