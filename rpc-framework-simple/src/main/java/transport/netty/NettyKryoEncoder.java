package transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import serialize.Serializer;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 18:23
 * @Description: Kryo加密器
 */
@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder {
    private Serializer serializer;
    // TODO: 2022/6/15 genericClass具体为什么类 
    private Class<?> genericClass;

    /**
     * 将对象转为字符流写入channel中
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (genericClass.isInstance(msg)) {
            byte[] bytes = serializer.serialize(msg);
            int length = bytes.length;
            // TODO: 2022/6/15 为什么写到Bytebuf中 
            out.writeInt(length);
            out.writeBytes(bytes);
        }
    }
}
