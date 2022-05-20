import dto.RPCRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class RpcClient {
    public static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    /**
     * �ͻ��˷��������������
     * @param rpcRequest ���͵�����
     * @param host ����
     * @param port �˿ں�
     * @return ���ص��Ƕ�����Ϊ���͵��Ƕ���
     */
    public Object sendRpcRequest(RPCRequest rpcRequest,String host,int port){
        try (Socket socket = new Socket(host, port)) {
            //todo Ϊʲô��socket
            ObjectOutputStream objectOutputStream =new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        /**
         * ���û�н���socket�ɹ��ͷ���null������������null�Ͳ�����ʾ�ͻ�������
         */
        return null;
    }
    
}
