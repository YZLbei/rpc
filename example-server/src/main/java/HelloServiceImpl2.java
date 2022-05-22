import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl2 implements HelloService,Service{
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl2.class);
    @Override
    public String Hello(Hello hello) {
        logger.info("HelloServiceImpl2 ’µΩ£∫{}.",hello.getMessage());
        String result = "Hello description is "+hello.getDescription();
        logger.info("HelloServiceImpl2∑µªÿ£∫{}.",result);
        return result;
    }
    
}
