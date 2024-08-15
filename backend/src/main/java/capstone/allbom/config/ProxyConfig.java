//package capstone.allbom.config;
//
//import capstone.allbom.config.aspect.TimeTraceAspect;
//import org.aopalliance.intercept.MethodInterceptor;
//import org.aopalliance.intercept.MethodInvocation;
//import org.springframework.aop.framework.ProxyFactory;
//import org.springframework.aop.support.DefaultPointcutAdvisor;
//import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
//
//@Configuration
//public class ProxyConfig {
//
//    @Bean
//    public BeanPostProcessor beanPostProcessor(TimeTraceAspect timeTraceAspect) {
//        return new BeanPostProcessor() {
//            @Override
//            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//                // Only apply to beans annotated with @Controller or @Service
//                if (AnnotationUtils.findAnnotation(bean.getClass(), Controller.class) != null ||
//                        AnnotationUtils.findAnnotation(bean.getClass(), Service.class) != null) {
//                    ProxyFactory proxyFactory = new ProxyFactory(bean);
//                    proxyFactory.addAdvisor(new DefaultPointcutAdvisor(
//                            new AnnotationMatchingPointcut(null, null),
//                            new MethodInterceptor() {
//                                @Override
//                                public Object invoke(MethodInvocation methodInvocation) throws Throwable {
//                                    return timeTraceAspect.traceTime(() -> methodInvocation.proceed());
//                                }
//                            }));
//                    return proxyFactory.getProxy();
//                }
//                return bean;
//            }
//        };
//    }
//}
