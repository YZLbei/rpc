package registry;

import enumeration.RpcErrorMessageEnum;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServiceRegistry implements ServiceRegistry{
    private  static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

    /**
     * �ӿںͷ���Ķ�Ӧ��ϵ��todo ����һ���ӿڱ�����ʵ����ʵ�ֵ����
     * key:service/interface name ,��serviceÿһ��ʵ�ֵĽӿڶ�ע�����
     * value:service
     * @param service
     * @param <T>
     */
    //todo Ϊʲô��ConcurrentHashMap
    //todo Ϊʲô����fianl 
    private final Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    //todo ʲô��˼
    //�Ѿ�ע��ķ���
    private final Set<String>  registeredService = ConcurrentHashMap.newKeySet();


    /**
     * todo �޸�Ϊɨ��ע��ע��
     * todo Ϊʲô��synchronized
     * ��������������ʵ�ֵĽӿڶ�ע���ȥ
     * @param service
     * @param <T>
     */
    @Override
    public synchronized <T> void registry(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)){
            return;
        }
        registeredService.add(serviceName);
        Class[]interfaces = service.getClass().getInterfaces();
        if (interfaces.length==0){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class i :
                interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }
        logger.info("Add Service:{} and interfaces:{}",serviceName,service.getClass().getInterfaces());
    }

    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service==null){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }
}
