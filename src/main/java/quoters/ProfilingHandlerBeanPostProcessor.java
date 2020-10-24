package quoters;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {

    private ProfilingController controller = new ProfilingController();
    private Map<String, Class> map = new HashMap<String, Class>();

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (controller.isEnabled()) {
            Class<?> beanClass = bean.getClass();
            if (beanClass.isAnnotationPresent(Profiling.class)) {
                map.put(beanName, beanClass);
            }
        }
        return bean;
    }

    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        Class aClass = map.get(beanName);
        if (aClass != null) {
            return Proxy.newProxyInstance(aClass.getClassLoader(), aClass.getInterfaces(), new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    System.out.println("Start profiling " + method.getName());
                    long before = System.nanoTime();
                    Object retVal = method.invoke(bean, args);
                    long after = System.nanoTime();
                    System.out.println("Method took " + (after - before));
                    System.out.println("Finish profiling ...");
                    return retVal;
                }
            });
        }
        return bean;
    }
}
