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
     * 接口和服务的对应关系，todo 处理一个接口被两个实现类实现的情况
     * key:service/interface name ,是service每一个实现的接口都注册进来
     * value:service
     * @param service
     * @param <T>
     */
    //todo 为什么用ConcurrentHashMap
    //todo 为什么都用fianl 
    private final Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    //todo 什么意思
    //已经注册的服务
    private final Set<String>  registeredService = ConcurrentHashMap.newKeySet();


    /**
     * todo 修改为扫描注解注册
     * todo 为什么用synchronized
     * 将这个对象的所有实现的接口都注册进去
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
