package demo.im.socket.io.demo;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * 客户端每隔两秒发送一个带有时间戳的 "hello world" 给服务端，服务端收到之后打印
 */
public class IOClient {

    public static void main(String[] args) throws Exception {

        // 启一个线程，不阻塞客户端的 “主线程”
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                while (true) {
                    try {
                        socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        // 这里try-catch，当服务器异常时，处理socket写入异常，java.net.SocketException: Connection reset by peer: socket write error
                    }
                }
            } catch (IOException e) {
                // 处理Socket连接异常，java.net.ConnectException: Connection refused: connect
            }
        }).start();

        // 其他服务...
    }
}
