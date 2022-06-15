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
 * @Description: kryo������
 */
@AllArgsConstructor
public class NettyKryoDecoder extends ByteToMessageDecoder {
    private final static Logger logger = LoggerFactory.getLogger(NettyKryoDecoder.class);
    private Serializer serializer;
    private Class<?> genericClass;
    /**
     * Netty�������Ϣ����Ҳ���Ƕ������л����Ӧ���ֽ�����Ĵ�С���洢�� ByteBuf ͷ��
     * ��Bytebufǰ�ĸ��ֽ�Ϊbody�ĳ���
     */
    private static final int BODY_LENGTH = 4;
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //1.byteBuf��д�����Ϣ������ռ���ֽ����Ѿ���4�ˣ����� byteBuf �Ŀɶ��ֽڱ������ 4��
        if (in.readableBytes()>=BODY_LENGTH){
            //2.���readIndex��λ�ã��Ա��������readIndex ��ʱ��ʹ��
            in.markReaderIndex();
            //3.���ݵĳ���
            int length = in.readInt();
            //4.��������������ֱ�� return
            if (length < 0 || in.readableBytes() < 0) {
                return;
            }
            //5.��Ϣ������
            // TODO: 2022/6/15 ������LengthFieldBasedFrameDecoder
            if (in.readableBytes()<length){
                in.resetReaderIndex();
                return;
            }
            byte[]body  = new byte[length];
            //��ByteBuf�е����ݶ���body��
            in.readBytes(body);
            Object obj = serializer.deserialize(body, genericClass);
            out.add(obj);


        }
    }
}
