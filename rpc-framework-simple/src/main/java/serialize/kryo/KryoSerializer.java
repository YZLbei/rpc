package serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dto.RpcRequest;
import dto.RpcResponse;
import exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialize.Serializer;

import java.io.*;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 17:01
 * @Description:kryo���л�����
 */
public class KryoSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    /**
     * ����kryo�����̰߳�ȫ�ģ�ÿ���̶߳�Ӧ�����Լ���Kryo��Input��OutPut
     * ����ʹ��ThreadLocal���kryo����
     *
     */
    
    // TODO: 2022/6/15 ��һ�� 
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        //Ĭ��ֵΪtrue,�Ƿ�ر�ע����Ϊ,�ر�֮����ܴ������л����⣬һ���Ƽ�����Ϊ true
        kryo.setReferences(true);
        //Ĭ��ֵΪfalse,�Ƿ�ر�ѭ�����ã�����������ܣ�����һ�㲻�Ƽ�����Ϊ true
        kryo.setRegistrationRequired(false);
        return kryo;
    });
    
    @Override
    public byte[] serialize(Object obj) {
        //�ֽ����������������ֽ����黺����
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             Output output = new Output(bos)) {
            Kryo kryo = kryoThreadLocal.get();
            // Object->byte:���������л�Ϊbyte����
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            logger.error("occur exception when serialize:", e);
            throw new SerializeException("���л�ʧ��");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            Input input = new Input(bis);
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readClassAndObject(input);
            kryoThreadLocal.remove();
            return clazz.cast(o);

        } catch (IOException e) {
            logger.error("occur exception when serialize:", e);
            throw new SerializeException("���л�ʧ��");
        }
    }
}
