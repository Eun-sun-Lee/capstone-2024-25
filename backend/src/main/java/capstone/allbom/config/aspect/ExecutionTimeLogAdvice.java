package capstone.allbom.config.aspect;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class ExecutionTimeLogAdvice implements MethodInterceptor {
    private final LogAspect logAspect;
    private final Class<?> typeToLog;
    private final String logGroup;

    protected ExecutionTimeLogAdvice(final LogAspect logAspect, final Class<?> typeToLog, final String logGroup) {
        this.logAspect = logAspect;
        this.typeToLog = typeToLog;
        this.logGroup = logGroup;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime = System.nanoTime();
        final Object result = invocation.proceed();
        long endTime = System.nanoTime();
        long timeTaken = endTime - startTime;

        Method method = invocation.getMethod();
        logAspect.logExecutionInfo(typeToLog, method, timeTaken, logGroup);

        return result;
    }
}
