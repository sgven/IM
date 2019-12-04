package demo.im.socket.io.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * IO 编程模型在客户端较少的情况下运行良好，但是如果单机服务端需要支撑成千上万的连接，IO 模型可能就不太合适了
 * 原因是：
 * 在传统的 IO 模型中，每个连接创建成功之后都需要一个线程来维护（解决阻塞读阻塞了其他客户端连接的问题），
 * 每个线程包含一个 while 死循环（客户端一直在输出的话，就相当于while死循环，不必纠结客户端是否一直输出，
 *      我们可以这样理解： “阻塞读的这一段时间内 就相当于while死循环，这个while死循环就是阻塞读，阻塞读时线程处于阻塞状态”），
 * 那么 1w 个连接对应 1w 个线程，继而 1w 个 while 死循环，这就带来几个问题：
 * 1.线程资源受限：线程是操作系统中非常宝贵的资源，同一时刻有大量的线程处于阻塞状态是非常严重的资源浪费，操作系统耗不起
 * 2.线程切换效率低下：单机 CPU 核数固定，线程爆炸之后操作系统频繁进行线程切换，应用性能急剧下降。
 * 3.IO 编程中，数据读写是以字节流为单位。——>以字节流读写有什么问题？
 *  - 每次处理一个或多个字节，效率很慢
 * 	- 读完之后流无法再读取
 * 	- 需要自己缓存数据
 *
 * 为了解决这三个问题，JDK 在 1.4 之后提出了 NIO。
 */
public class IOServer {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8000);
        // 接收新连接线程（为什么这里启一个线程，作用是不会阻塞 “主线程”）
        new Thread(() -> {
            while (true) {
                // 阻塞式的接收客户端连接（accept方法会阻塞，直到接收到一个客户都连接，返回socket）
                try {
                    Socket socket = serverSocket.accept();
                    /**
                     * 为什么这里要启线程呢？
                     * 因为读数据是阻塞读，而且我们模拟的IOClient是一直不停地写数据的，
                     * 所以，如果不启线程来读的话，服务端就会一直阻塞在第一个客户端连接socket上，即阻塞在read方法上，不会进入到serverSocket.accept()方法来接收其他的客户端连接了
                     * 实际上，（不管客户端是不是一直在写数据）这里启线程来读数据，目的就是为了阻塞读 “不会阻塞到其他客户端”；
                     * 这样做服务端才能同一时刻并发处理多个客户端连接，否则，就只能一个一个顺序的处理客户端连接了。
                     */
                    // 每一个客户端连接都启一个新线程，来读取数据
                    new Thread(() -> {
                        try {
                            // 获取输入流
                            InputStream inputStream = socket.getInputStream();
                            byte[] bytesBuffer = new byte[1024];
                            int len;
                            // 按字节流方式读取数据，阻塞式的读，读取到的数据会存储到bytesBuffer中，返回的int表示读取到的长度
                            while((len = inputStream.read(bytesBuffer)) != -1) { // -1表示读取到流的末尾
                                System.out.println(new String(bytesBuffer, 0, len));
                            }
                        } catch (IOException e) {
                        }
                    }).start();
                } catch (IOException e) {
                    // 处理客户端连接异常，java.net.SocketException: Connection reset
                }
            }
        }).start();
    }
}
