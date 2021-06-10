package demo.im.netty.learn02;

import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

/*public class FirstClientHandler implements ChannelHandler {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}*/

public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 1.channelActive()方法 在客户端连接建立成功之后被调用
     * 2.在这里编写向服务端写数据的逻辑
     * 3.写数据的逻辑分为两步：
     *      1. 获取一个 netty 对二进制数据的抽象 ByteBuf
     *      2. 把数据写到服务端
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        System.out.println(new Date() + ": 客户端写出数据");

        // 1.获取数据
        ByteBuf buffer = getByteBuf(ctx);

        // 2.写数据
        ctx.channel().writeAndFlush(buffer);
    }

    /**
     * ctx.alloc() 获取到一个(ByteBuf的)内存管理器ByteBufAllocator，
     * 它的作用就是分配一个 ByteBuf，然后我们把字符串的二进制数据填充到 ByteBuf
     * 然后我们就获取到了 Netty 需要的一个数据格式
     * @param ctx
     * @return
     */
    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1.获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2.准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "你好, netty!".getBytes(Charset.forName("utf-8"));

        // 3.填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);

        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }
}