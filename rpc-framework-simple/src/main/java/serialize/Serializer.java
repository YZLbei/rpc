package serialize;

/**
 * ���л��ӿڣ��������л��඼Ҫʵ������ӿ�
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 16:13
 * @Description: ���л��ӿ���
 */
public interface Serializer {
    /**
     * ���л�
     *
     * @param obj Ҫ���л��Ķ���
     * @return �ֽ�����
     */
    byte[] serialize(Object obj);

    /**
     * �����л�
     *
     * @param bytes ���л�����ֽ�����
     * @param clazz Ŀ����
     * @param <T>   ������͡��ٸ�����,  {@code String.class} �������� {@code Class<String>}.
     *              �����֪��������͵Ļ���ʹ�� {@code Class<?>}
     * @return �����л��Ķ���
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
