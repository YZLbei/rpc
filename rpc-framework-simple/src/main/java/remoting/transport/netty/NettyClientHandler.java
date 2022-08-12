package remoting.transport.netty;

import dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 18:55
 * @Description: ������������ص�RpcResponse
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //���������صĶ���
            RpcResponse rpcResponse = (RpcResponse) msg;
            logger.info(String.format("client receive msg: %s", rpcResponse));
            // TODO: 2022/6/15 ����
            // ����һ�� AttributeKey ����
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            // ������˵ķ��ؽ�����浽 AttributeMap �ϣ�AttributeMap ���Կ�����һ��Channel�Ĺ�������Դ
            // AttributeMap��key��AttributeKey��value��Attribute
            ctx.channel().attr(key).set(rpcResponse);
            ctx.channel().close();
            
        }finally {
            // TODO: 2022/6/15 ɶ���� 
            ReferenceCountUtil.release(msg);
        }
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
