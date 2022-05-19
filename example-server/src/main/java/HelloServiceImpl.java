import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl implements HelloService{
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String Hello(Hello hello) {
        logger.info("HelloServiceImpl�յ���{}.",hello.getMessage());
        String result = "Hello description is "+hello.getDescription();
        logger.info("HelloServiceImpl���أ�{}.",result);
        return result;
    }
    
}
