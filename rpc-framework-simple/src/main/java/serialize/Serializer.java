package serialize;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 16:13
 * @Description: ���л��ӿ���
 */
public interface Serializer {
    /**
     * ���л�
     * 
     * @param obj Ҫ���л��Ķ���
     * @return ת��Ϊ�ֽ�����
     */
    byte[]serialize(Object obj);

    /**
     * �����л�
     * @param clazz ��
     * @param <T>
     * @return �����л��Ķ���
     */
    <T> T deserialize(byte[] bytes,Class<T>clazz);
}
