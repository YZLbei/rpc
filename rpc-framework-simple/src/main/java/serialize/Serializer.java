package serialize;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 16:13
 * @Description: 序列化接口类
 */
public interface Serializer {
    /**
     * 序列化
     * 
     * @param obj 要序列化的对象
     * @return 转化为字节数组
     */
    byte[]serialize(Object obj);

    /**
     * 反序列化
     * @param clazz 类
     * @param <T>
     * @return 反序列化的对象
     */
    <T> T deserialize(byte[] bytes,Class<T>clazz);
}
