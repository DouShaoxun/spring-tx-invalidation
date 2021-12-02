package cn.cruder.springtxinvalidation.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 执行时间
 *
 * @Author: cruder
 * @Date:r2021/12/02/21:02
 */
@Slf4j
@Aspect
@Component
//@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class AopExecutionTimeAspect {

    /**
     * 指定到方法
     * 切入点
     */
    @Pointcut("execution(* cn.cruder.springtxinvalidation.service.AccountInfoServiceC.transferA(..))")
    public void aspectPointcut() {
    }

    /**
     * 环绕
     * <p>
     * {@see EnableTransactionManagement#order}
     *
     * @param point 切入点
     * @return result
     * @throws Throwable
     */
    @Around("aspectPointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long startTime = System.currentTimeMillis();
        try {
            result = point.proceed();
        } catch (Throwable e) {
            log.error("doAround", e);
            result = e.getMessage();
            // 如果切面捕获了异常，但是没有抛出，可能会导致事务不会回滚

            // 解决方案一：推荐
            // throw e;

            // 解决方案二
            // TransactionInterceptor.currentTransactionStatus().setRollbackOnly();

            // 解决方案三
            // 调整切面顺序，但是不推荐
            // 自定义切面默认优先级是最低，EnableTransactionManagement.order 默认优先级最低；但是自定义切面会在事务切面之前执行 如果此时没有抛出异常，则事务切面就不会捕获异常，也不会回滚
            // 将自定义切面 优先级调到比事务切面低即可  例如 @Order(Ordered.LOWEST_PRECEDENCE - 1)

        } finally {
            long endTime = System.currentTimeMillis();
            String declaringTypeName = point.getSignature().getDeclaringTypeName();
            String sigName = point.getSignature().getName();
            log.info(declaringTypeName + "." + sigName + " execution time :" + (endTime - startTime) + " ms.");
        }
        return result;
    }

}
