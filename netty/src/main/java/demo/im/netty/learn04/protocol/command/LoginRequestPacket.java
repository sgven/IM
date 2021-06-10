package demo.im.netty.learn04.protocol.command;

import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {

    private Integer userId;

    private String username;

    private String password;

    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
