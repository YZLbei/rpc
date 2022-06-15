package transport.socket;

import dto.RpcRequest;
import dto.RpcResponse;
import enumeration.RpcErrorMessageEnum;
import enumeration.RpcResponseCode;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcClient {
    public static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    /**
     * �ͻ��˷��������������
     * @param rpcRequest ���͵�����
     * @param host ����
     * @param port �˿ں�
     * @return ���ص��Ƕ�����Ϊ���͵��Ƕ���
     */
    public Object sendRpcRequest(RpcRequest rpcRequest, String host, int port){
        try (Socket socket = new Socket(host, port)) {
            //Ϊʲô��socket
            ObjectOutputStream objectOutputStream =new ObjectOutputStream(socket.getOutputStream());
            // �������Զ����Ƹ�ʽд��֮����ô�������أ�
            //ʹ��socket�����ݵ�
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            
            //����������������Ӧ����
            RpcResponse rpcResponse  = (RpcResponse)  objectInputStream.readObject();
            if(rpcResponse==null){
                logger.error("���÷���ʧ�ܣ�serviceName:{}",rpcRequest.getInterfaceName());
                throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,"interfaceName:" + rpcRequest.getInterfaceName());
            }
            if(rpcResponse.getCode()==null||!rpcResponse.getCode().equals(RpcResponseCode.SUCCESS.getCode())){
                logger.error("���÷���ʧ�ܣ�serviceName:{},RpcResponse:{}",rpcRequest.getInterfaceName(),rpcResponse);
                throw  new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,"interfaceName:"+rpcRequest.getInterfaceName());
            }
            
            return rpcResponse.getData();
            
            
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("���÷���ʧ��:", e);
        }
        /**
         * ���û�н���socket�ɹ��ͷ���null������������null�Ͳ�����ʾ�ͻ�������
         */
    }
    
}
