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
 * @Description: 处理服务器返回的RpcResponse
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //服务器返回的对象
            RpcResponse rpcResponse = (RpcResponse) msg;
            logger.info(String.format("client receive msg: %s", rpcResponse));
            // TODO: 2022/6/15 不懂
            // 声明一个 AttributeKey 对象
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            // 将服务端的返回结果保存到 AttributeMap 上，AttributeMap 可以看作是一个Channel的共享数据源
            // AttributeMap的key是AttributeKey，value是Attribute
            ctx.channel().attr(key).set(rpcResponse);
            ctx.channel().close();
            
        }finally {
            // TODO: 2022/6/15 啥东西 
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
