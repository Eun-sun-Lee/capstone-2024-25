package capstone.allbom.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;

import org.slf4j.MDC;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Slf4j
@Component
@Aspect
public class LogAspect {
    public static final String ALLBOM_PUBLIC_METHOD_POINTCUT_EXPRESSION = "execution(public * capstone.allbom..*(..))";

    void logExecutionInfo(Class<?> typeToLog, Method method, long timeTaken, String logGroup) {
        String traceId = MDC.get("traceId");

        log.debug("{} took {} ms. (info group: '{}', traceId: {})",
                value("method", typeToLog.getName() + "." + method.getName() + "()"),
                value("execution_time", timeTaken),
                value("group", logGroup),
                value("traceId", traceId));
    }

    private String value(String key, Object value) {
        return key + "=" + value;
    }


    Object createLogProxy(Object target, Class<?> typeToLog, String logGroup) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(ALLBOM_PUBLIC_METHOD_POINTCUT_EXPRESSION);

        ExecutionTimeLogAdvice advice = new ExecutionTimeLogAdvice(this, typeToLog, logGroup);
        advisor.setAdvice(advice);

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvisor(advisor);
        proxyFactory.setProxyTargetClass(true);

        return proxyFactory.getProxy();
    }
}