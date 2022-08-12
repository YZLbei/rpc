package remoting.transport.socket;

import enumeration.RpcErrorMessageEnum;
import enumeration.RpcResponseCode;
import exception.RpcException;
import extension.ExtensionLoader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceDiscovery;
import remoting.dto.RpcRequest;
import remoting.dto.RpcResponse;
import remoting.transport.RpcRequestTransport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * ���� Socket ���� RpcRequest
 */
@AllArgsConstructor
@Slf4j
public class SocketRpcClient implements RpcRequestTransport {
    //public static final Logger logger = LoggerFactory.getLogger(SocketRpcClient.class);
//    private String host;
//    private int port;
//    /**
//     * �ͻ��˷��������������
//     * @param rpcRequest ���͵�����
//     * @return ���ص��Ƕ�����Ϊ���͵��Ƕ���
//     */
//    public Object sendRpcRequest(RpcRequest rpcRequest){
//        try (Socket socket = new Socket(host, port)) {
//            //Ϊʲô��socket
//            ObjectOutputStream objectOutputStream =new ObjectOutputStream(socket.getOutputStream());
//            // �������Զ����Ƹ�ʽд��֮����ô�������أ�
//            //ʹ��socket�����ݵ�
//            objectOutputStream.writeObject(rpcRequest);
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            
//            //����������������Ӧ����
//            RpcResponse rpcResponse  = (RpcResponse)  objectInputStream.readObject();
//            if(rpcResponse==null){
//                logger.error("���÷���ʧ�ܣ�serviceName:{}",rpcRequest.getInterfaceName());
//                throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,"interfaceName:" + rpcRequest.getInterfaceName());
//            }
//            if(rpcResponse.getCode()==null||!rpcResponse.getCode().equals(RpcResponseCode.SUCCESS.getCode())){
//                logger.error("���÷���ʧ�ܣ�serviceName:{},RpcResponse:{}",rpcRequest.getInterfaceName(),rpcResponse);
//                throw  new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,"interfaceName:"+rpcRequest.getInterfaceName());
//            }
//            
//            return rpcResponse.getData();
//            
//            
//        } catch (IOException | ClassNotFoundException e) {
//            throw new RpcException("���÷���ʧ��:", e);
//        }
//        /**
//         * ���û�н���socket�ɹ��ͷ���null������������null�Ͳ�����ʾ�ͻ�������
//         */
//    }




    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient() {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Send data to the server through the output stream
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Read RpcResponse from the input stream
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("���÷���ʧ��:", e);
        }
    }
    
}
