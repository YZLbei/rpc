//package remoting.transport.netty;
//
//import dto.RpcRequest;
//import dto.RpcResponse;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelFutureListener;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import registry.DefaultServiceRegistry;
//import registry.ServiceRegistry;
//import remoting.handler.RpcRequestHandler;
//
///**
// * @Auther: YuZhenLong
// * @Date: 2022/6/15 19:08
// * @Description: 
// */
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
//    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
//    private static RpcRequestHandler rpcRequestHandler;
//    private static ServiceRegistry serviceRegistry;
//    static {
//        rpcRequestHandler = new RpcRequestHandler();
//        serviceRegistry = new DefaultServiceRegistry();
//    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        try {
//            RpcRequest rpcRequest = (RpcRequest) msg;
//            logger.info(String.format("server receive msg: %s", rpcRequest));
//            String interfaceName = rpcRequest.getInterfaceName();
//            Object service = serviceRegistry.getService(interfaceName);
//            Object result = rpcRequestHandler.handle(rpcRequest, service);
//            logger.info(String.format("server get result: %s", result.toString()));
//            ChannelFuture channel = ctx.writeAndFlush(RpcResponse.success(result));
//            // TODO: 2022/6/15 为啥 
//            channel.addListener(ChannelFutureListener.CLOSE);
//        }finally {
//            
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        logger.error("server catch exception");
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
