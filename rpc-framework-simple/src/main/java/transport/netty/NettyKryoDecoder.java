package transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialize.Serializer;

import java.util.List;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 18:22
 * @Description: kryo解码器
 */
@AllArgsConstructor
public class NettyKryoDecoder extends ByteToMessageDecoder {
    private final static Logger logger = LoggerFactory.getLogger(NettyKryoDecoder.class);
    private Serializer serializer;
    private Class<?> genericClass;
    /**
     * Netty传输的消息长度也就是对象序列化后对应的字节数组的大小，存储在 ByteBuf 头部
     * 即Bytebuf前四个字节为body的长度
     */
    private static final int BODY_LENGTH = 4;
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //1.byteBuf中写入的消息长度所占的字节数已经是4了，所以 byteBuf 的可读字节必须大于 4，
        if (in.readableBytes()>=BODY_LENGTH){
            //2.标记readIndex的位置，以便后面重置readIndex 的时候使用
            in.markReaderIndex();
            //3.内容的长度
            int length = in.readInt();
            //4.遇到不合理的情况直接 return
            if (length < 0 || in.readableBytes() < 0) {
                return;
            }
            //5.消息不完整
            // TODO: 2022/6/15 可以用LengthFieldBasedFrameDecoder
            if (in.readableBytes()<length){
                in.resetReaderIndex();
                return;
            }
            byte[]body  = new byte[length];
            //将ByteBuf中的内容读到body中
            in.readBytes(body);
            Object obj = serializer.deserialize(body, genericClass);
            out.add(obj);


        }
    }
}
