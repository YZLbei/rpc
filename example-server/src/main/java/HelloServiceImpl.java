import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl implements HelloService{
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String Hello(Hello hello) {
        logger.info("HelloServiceImpl ’µΩ£∫{}.",hello.getMessage());
        String result = "Hello description is "+hello.getDescription();
        logger.info("HelloServiceImpl∑µªÿ£∫{}.",result);
        return result;
    }
    
}
