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
 * @Description:kryo序列化方法
 */
public class KryoSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    /**
     * 由于kryo不是线程安全的，每个线程都应该有自己的Kryo，Input和OutPut
     * 所以使用ThreadLocal存放kryo对象
     *
     */
    
    // TODO: 2022/6/15 看一下 
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        //默认值为true,是否关闭注册行为,关闭之后可能存在序列化问题，一般推荐设置为 true
        kryo.setReferences(true);
        //默认值为false,是否关闭循环引用，可以提高性能，但是一般不推荐设置为 true
        kryo.setRegistrationRequired(false);
        return kryo;
    });
    
    @Override
    public byte[] serialize(Object obj) {
        //字节数组输入流创建字节数组缓冲区
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             Output output = new Output(bos)) {
            Kryo kryo = kryoThreadLocal.get();
            // Object->byte:将对象序列化为byte数组
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            logger.error("occur exception when serialize:", e);
            throw new SerializeException("序列化失败");
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
            throw new SerializeException("序列化失败");
        }
    }
}
