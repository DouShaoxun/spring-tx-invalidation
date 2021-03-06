package cn.cruder.springtxinvalidation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

/**
 * @author dousx
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    /**
     * 默认Transactional加在非public方法上不生效
     * <br/>
     * 设置{@link AnnotationTransactionAttributeSource#publicMethodsOnly}为false
     * <br/>
     * 则非public方法也可以生效
     * <br/>
     * 需要设置 spring.main.allow-bean-definition-overriding=true
     *
     * @return {@link AnnotationTransactionAttributeSource}
     */
    @Bean
    public TransactionAttributeSource transactionAttributeSource() {
        return new AnnotationTransactionAttributeSource(false);
    }

}
