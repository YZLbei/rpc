//package remoting.transport.netty;
//
//import dto.RpcRequest;
//import dto.RpcResponse;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import serialize.kryo.KryoSerializer;
//
///**
// * @Auther: YuZhenLong
// * @Date: 2022/6/15 19:45
// * @Description:
// */
//public class NettyRpcServer {
//    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);
//    private final int port;
//    private KryoSerializer kryoSerializer;
//    public NettyRpcServer(int port) {
//        this.port = port;
//        kryoSerializer = new KryoSerializer();
//    }
//    public void run() {
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap bootstrap = new ServerBootstrap();
//            bootstrap.group(bossGroup,workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
//                        @Override
//                        protected void initChannel(NioSocketChannel ch) throws Exception {
//                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
//                            ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcRequest.class));
//                            ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcResponse.class));
//                            ch.pipeline().addLast(new NettyServerHandler());
//                        }
//                    })
//                    // TODO: 2022/6/15 为什么 
//                    // 设置tcp缓冲区
//                    .childOption(ChannelOption.TCP_NODELAY, true)
//                    .option(ChannelOption.SO_BACKLOG, 128)
//                    .option(ChannelOption.SO_KEEPALIVE, true);
//            ChannelFuture future = bootstrap.bind(port).sync();
//            future.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            logger.error("occur exception when start server:", e);
//        }
//        finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//
//
//    }
//}
