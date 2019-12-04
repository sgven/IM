package demo.im.socket.nio.demo;

import java.net.Socket;
import java.util.Date;

public class IOClient2 {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8000);
        while (true) {
            socket.getOutputStream().write((new Date() + ": hello world 2").getBytes());
            Thread.sleep(2000);
        }

        // 其他服务...
    }
}
