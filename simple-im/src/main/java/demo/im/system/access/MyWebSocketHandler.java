package demo.im.system.access;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！" + ctx.channel().id());

        //添加到channelGroup通道组
        MyChannelHandlerPool.channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！" + ctx.channel().id());
        //从channelGroup中移除通道组
        MyChannelHandlerPool.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端消息处理："+ ctx.channel().id());
        //首次连接是FullHttpRequest，处理参数
        if (null != msg && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();

            Map paramMap=getUrlParams(uri);
            System.out.println("接收到的参数是："+ JSON.toJSONString(paramMap));
            //如果url包含参数，需要处理
            if(uri.contains("?")){
                String newUri=uri.substring(0,uri.indexOf("?"));
                System.out.println(newUri);
                request.setUri(newUri);
            }

            //验证用户id权限token
            //验证通过，绑定用户与channel连接通道
            ctx.channel().attr(AttributeKey.valueOf("user")).set(paramMap.get("uid"));
            ctx.channel().attr(AttributeKey.valueOf("tag")).set(paramMap.get("tag"));

        } else if(msg instanceof TextWebSocketFrame){
            //正常的TEXT消息类型
            TextWebSocketFrame frame=(TextWebSocketFrame)msg;
            System.out.println("客户端收到服务器数据：" + frame.text());
//            sendGroupAll(frame.text());
//            String[] values = frame.text().split(":");
//            send(values[0], values[1]);
            Map<String, String> params = getParams(frame.text());
            sendGroupByTag(params.get("msg"), params.get("tag"));
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }

    /**
     * 发送消息给指定用户
     * @param message
     * @param user
     */
    public void send(String message, String user) {
        if (StringUtils.isNotBlank(user)) {
            MyChannelHandlerPool.channelGroup.stream().forEach(ch -> {
                if (ch.attr(AttributeKey.valueOf("user")).get().equals(user)) {
                    ch.writeAndFlush(new TextWebSocketFrame(message));
                }
            });
        }
    }

    /**
     * 群发消息给指定用户
     * @param message
     * @param users
     */
    public void sendGroupByUsers(String message, List<String> users) {
        if (users != null && users.size() > 0) {
            MyChannelHandlerPool.channelGroup.stream().filter(ch -> {
                String user = (String) ch.attr(AttributeKey.valueOf("user")).get();
                if (users.contains(user)) {
                    return true;
                }
                return false;
            }).forEach(ch -> {
                ch.writeAndFlush(new TextWebSocketFrame(message));
            });
        }
    }

    /**
     * 按标签群发消息
     * @param message
     * @param tag
     */
    public void sendGroupByTag(String message, String tag) {
        if (StringUtils.isNotBlank(tag) && !"undefined".equals(tag)) {
            MyChannelHandlerPool.channelGroup.stream().filter(ch -> {
               if (ch.attr(AttributeKey.valueOf("tag")).get().equals(tag)) {
                   return true;
               }
               return false;
            }).forEach(ch -> {
                ch.writeAndFlush(new TextWebSocketFrame(message));
            });
        }
    }

    /**
     * 按多个标签群发消息
     * @param message
     * @param tags
     */
    public void sendGroupByTags(String message, List<String> tags) {
        if (tags != null && tags.size() > 0) {
            MyChannelHandlerPool.channelGroup.stream().filter(ch -> {
                String tag = (String) ch.attr(AttributeKey.valueOf("tag")).get();
                if (tags.contains(tag)) {
                   return true;
                }
                return false;
            }).forEach(ch -> {
                ch.writeAndFlush(new TextWebSocketFrame(message));
            });
        }
    }

    /**
     * 群发消息给所有人
     * @param message
     */
    public void sendGroupAll(String message){
        MyChannelHandlerPool.channelGroup.writeAndFlush(new TextWebSocketFrame(message));
    }

    private static Map getUrlParams(String url){
        Map<String,String> map = new HashMap<>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String params = url.split(";")[1];
            return getParams(params);

        }else{
            return map;
        }
    }

    private static Map getParams(String params) {
        Map<String, String> map = new HashMap<>();
        String[] arr = params.split("&");
        for (String s : arr){
            String key = s.split("=")[0];
            String value = s.split("=")[1];
            map.put(key,value);
        }
        return  map;
    }
}
