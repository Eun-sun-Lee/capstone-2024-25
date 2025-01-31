package capstone.allbom.config.aspect;

import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class LogProxyPostProcessor implements BeanPostProcessor {
    private final LogAspect logAspect;
    private final Set<Class<?>> typesAnnotatedWith;

    protected LogProxyPostProcessor(final LogAspect logAspect) {
        this.logAspect = logAspect;

        Reflections reflections = new Reflections("capstone.allbom");
        typesAnnotatedWith = Collections.unmodifiableSet(reflections.getTypesAnnotatedWith(LogMethodExecutionTime.class));
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return typesAnnotatedWith.stream()
                .filter(typeToLog -> typeToLog.isAssignableFrom(bean.getClass()))
                .findAny()
                .map(typeToLog -> createLogProxy(bean, typeToLog))
                .orElse(bean);
    }

    private Object createLogProxy(Object bean, Class<?> typeToLog) {
        LogMethodExecutionTime annotation = typeToLog.getAnnotation(LogMethodExecutionTime.class);
        String groupName = annotation.group();
        return logAspect.createLogProxy(bean, typeToLog, groupName);
    }
}
