package remoting.transport.netty;

import dto.RpcRequest;
import dto.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialize.kryo.KryoSerializer;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 19:27
 * @Description:
 */

public class NettyRpcClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);
    private String host;
    private int port;
    private static Bootstrap bootstrap;

    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        // RpcResponse -> ByteBuf
                        // TODO: 2022/6/15 为什么用RpcResponse.class 就行
                        pipeline.addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
                        // ByteBuf -> RpcRequest
                        pipeline.addLast(new NettyKryoEncoder(kryoSerializer,RpcRequest.class));
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    /**
     * 发送消息到服务器端
     * @param rpcRequest 消息体
     * @return 服务端返回的数据
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            logger.info("client connect  {}", host + ":" + port);
            Channel channel = channelFuture.channel();
            //连接建立成功
            if (channel!=null){
                channel.writeAndFlush(rpcRequest).addListener(future -> {
                   if (future.isSuccess()){
                       logger.info(String.format("client send message: %s", rpcRequest.toString()));
                   }
                   else {
                       logger.error("Send failed:", future.cause());
                   }
                });
                channel.close().sync();
                // TODO: 2022/6/15 不懂 
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }

        }catch (InterruptedException e) {
            logger.error("occur exception when connect server:", e);
        }
        return null;
        
    }
}
