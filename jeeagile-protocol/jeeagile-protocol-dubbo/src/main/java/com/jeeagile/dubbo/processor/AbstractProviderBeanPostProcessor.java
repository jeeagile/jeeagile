package com.jeeagile.dubbo.processor;

import com.jeeagile.core.protocol.annotation.dubbo.DubboProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.alibaba.spring.util.AnnotationUtils.getAnnotationAttributes;
import static org.springframework.aop.support.AopUtils.getTargetClass;
import static org.springframework.core.BridgeMethodResolver.findBridgedMethod;
import static org.springframework.core.BridgeMethodResolver.isVisibilityBridgeMethodPair;
import static org.springframework.core.GenericTypeResolver.resolveTypeArgument;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public abstract class AbstractProviderBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, PriorityOrdered,
        BeanFactoryAware, EnvironmentAware, DisposableBean {

    private final static int CACHE_SIZE = Integer.getInteger("", 32);

    private final Log logger = LogFactory.getLog(getClass());

    private final Class<? extends Annotation>[] annotationTypes;

    private final ConcurrentMap<String, AnnotatedInjectionMetadata> injectionMetadataCache =
            new ConcurrentHashMap<>(CACHE_SIZE);

    private final ConcurrentMap<String, Object> injectedObjectsCache = new ConcurrentHashMap<>(CACHE_SIZE);

    private ConfigurableListableBeanFactory beanFactory;

    private Environment environment;


    /**
     * make sure higher priority than {@link AutowiredAnnotationBeanPostProcessor}
     */
    private int order = Ordered.LOWEST_PRECEDENCE - 3;

    /**
     * @param annotationTypes the multiple types of {@link Annotation annotations}
     */
    public AbstractProviderBeanPostProcessor(Class<? extends Annotation>... annotationTypes) {
        Assert.notEmpty(annotationTypes, "The argument of annotations' types must not empty");
        this.annotationTypes = annotationTypes;
    }

    private static <T> Collection<T> combine(Collection<? extends T>... elements) {
        List<T> allElements = new ArrayList<T>();
        for (Collection<? extends T> e : elements) {
            allElements.addAll(e);
        }
        return allElements;
    }


    protected final Class<? extends Annotation>[] getAnnotationTypes() {
        return annotationTypes;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = findInjectionMetadata(beanName, bean.getClass(), pvs);
        try {
            metadata.inject(bean, beanName, pvs);
        } catch (BeanCreationException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of dependencies is failed", ex);
        }
        return pvs;
    }

    /**
     * Finds {@link InjectionMetadata.InjectedElement} Metadata from annotated fields
     *
     * @param beanClass The {@link Class} of Bean
     * @return non-null {@link List}
     */
    private List<AnnotatedFieldElement> findFieldAnnotationMetadata(final Class<?> beanClass) {

        final List<AnnotatedFieldElement> elements = new LinkedList<AnnotatedFieldElement>();

        ReflectionUtils.doWithFields(beanClass, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                for (Class<? extends Annotation> annotationType : getAnnotationTypes()) {
                    AnnotationAttributes attributes = getAnnotationAttributes(field, annotationType, getEnvironment(), true, true, new String[0]);
                    if (attributes != null) {
                        attributes = getDubboProviderAnnotationAttributes(attributes);
                        if (Modifier.isStatic(field.getModifiers())) {
                            if (logger.isWarnEnabled()) {
                                logger.warn("@" + annotationType.getName() + " is not supported on static fields: " + field);
                            }
                            return;
                        }
                        elements.add(new AnnotatedFieldElement(field, attributes));
                    }
                }
            }
        });
        return elements;
    }

    /**
     * Finds {@link InjectionMetadata.InjectedElement} Metadata from annotated methods
     *
     * @param beanClass The {@link Class} of Bean
     * @return non-null {@link List}
     */
    private List<AnnotatedMethodElement> findAnnotatedMethodMetadata(final Class<?> beanClass) {
        final List<AnnotatedMethodElement> elements = new LinkedList<AnnotatedMethodElement>();
        ReflectionUtils.doWithMethods(beanClass, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                Method bridgedMethod = findBridgedMethod(method);
                if (!isVisibilityBridgeMethodPair(method, bridgedMethod)) {
                    return;
                }
                for (Class<? extends Annotation> annotationType : getAnnotationTypes()) {
                    AnnotationAttributes attributes = getAnnotationAttributes(bridgedMethod, annotationType, getEnvironment(), true, true);
                    if (attributes != null && method.equals(ClassUtils.getMostSpecificMethod(method, beanClass))) {
                        attributes = getDubboProviderAnnotationAttributes(attributes);
                        if (Modifier.isStatic(method.getModifiers())) {
                            if (logger.isWarnEnabled()) {
                                logger.warn("@" + annotationType.getName() + " annotation is not supported on static methods: " + method);
                            }
                            return;
                        }
                        if (method.getParameterTypes().length == 0) {
                            if (logger.isWarnEnabled()) {
                                logger.warn("@" + annotationType.getName() + " annotation should only be used on methods with parameters: " +
                                        method);
                            }
                        }
                        PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, beanClass);
                        elements.add(new AnnotatedMethodElement(method, pd, attributes));
                    }
                }
            }
        });
        return elements;
    }

    private AnnotationAttributes getDubboProviderAnnotationAttributes(AnnotationAttributes annotationAttributes) {
        if (annotationAttributes.containsKey("dubboProvider")) {
            DubboProvider dubboProvider = annotationAttributes.getAnnotation("dubboProvider", DubboProvider.class);
            AnnotationAttributes dubboAnnotationAttributes = getAnnotationAttributes(dubboProvider, getEnvironment(), true, new String[0]);
            annotationAttributes.putAll(dubboAnnotationAttributes);
            annotationAttributes.remove("dubboProvider");
            annotationAttributes.remove("rabbitProvider");
        }
        return annotationAttributes;
    }

    private AnnotatedInjectionMetadata buildAnnotatedMetadata(final Class<?> beanClass) {
        Collection<AnnotatedFieldElement> fieldElements = findFieldAnnotationMetadata(beanClass);
        Collection<AnnotatedMethodElement> methodElements = findAnnotatedMethodMetadata(beanClass);
        return new AnnotatedInjectionMetadata(beanClass, fieldElements, methodElements);
    }

    private InjectionMetadata findInjectionMetadata(String beanName, Class<?> clazz, PropertyValues pvs) {
        // Fall back to class name as cache key, for backwards compatibility with custom callers.
        String cacheKey = (StringUtils.hasLength(beanName) ? beanName : clazz.getName());
        // Quick check on the concurrent map first, with minimal locking.
        AnnotatedInjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
        if (InjectionMetadata.needsRefresh(metadata, clazz)) {
            synchronized (this.injectionMetadataCache) {
                metadata = this.injectionMetadataCache.get(cacheKey);
                if (InjectionMetadata.needsRefresh(metadata, clazz)) {
                    if (metadata != null) {
                        metadata.clear(pvs);
                    }
                    try {
                        metadata = buildAnnotatedMetadata(clazz);
                        this.injectionMetadataCache.put(cacheKey, metadata);
                    } catch (NoClassDefFoundError err) {
                        throw new IllegalStateException("Failed to introspect object class [" + clazz.getName() +
                                "] for annotation metadata: could not find class that it depends on", err);
                    }
                }
            }
        }
        return metadata;
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        if (beanType != null) {
            InjectionMetadata metadata = findInjectionMetadata(beanName, beanType, null);
            metadata.checkConfigMembers(beanDefinition);
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void destroy() throws Exception {

        for (Object object : injectedObjectsCache.values()) {
            if (logger.isInfoEnabled()) {
                logger.info(object + " was destroying!");
            }

            if (object instanceof DisposableBean) {
                ((DisposableBean) object).destroy();
            }
        }

        injectionMetadataCache.clear();
        injectedObjectsCache.clear();

        if (logger.isInfoEnabled()) {
            logger.info(getClass() + " was destroying!");
        }

    }

    protected Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    /**
     * Get injected-object from specified {@link AnnotationAttributes annotation attributes} and Bean Class
     *
     * @param attributes      {@link AnnotationAttributes the annotation attributes}
     * @param bean            Current bean that will be injected
     * @param beanName        Current bean name that will be injected
     * @param injectedType    the type of injected-object
     * @param injectedElement {@link InjectionMetadata.InjectedElement}
     * @return An injected object
     * @throws Exception If getting is failed
     */
    protected Object getInjectedObject(AnnotationAttributes attributes, Object bean, String beanName, Class<?> injectedType,
                                       InjectionMetadata.InjectedElement injectedElement) throws Exception {

        String cacheKey = buildInjectedObjectCacheKey(attributes, bean, beanName, injectedType, injectedElement);

        Object injectedObject = injectedObjectsCache.get(cacheKey);

        if (injectedObject == null) {
            injectedObject = doGetInjectedBean(attributes, bean, beanName, injectedType, injectedElement);
            // Customized inject-object if necessary
            injectedObjectsCache.putIfAbsent(cacheKey, injectedObject);
        }

        return injectedObject;

    }


    protected abstract Object doGetInjectedBean(AnnotationAttributes attributes, Object bean, String beanName, Class<?> injectedType,
                                                InjectionMetadata.InjectedElement injectedElement) throws Exception;


    protected abstract String buildInjectedObjectCacheKey(AnnotationAttributes attributes, Object bean, String beanName,
                                                          Class<?> injectedType,
                                                          InjectionMetadata.InjectedElement injectedElement);


    /**
     * {@link Annotation Annotated} {@link InjectionMetadata} implementation
     */
    private class AnnotatedInjectionMetadata extends InjectionMetadata {

        private final Collection<AnnotatedFieldElement> fieldElements;
        private final Collection<AnnotatedMethodElement> methodElements;

        public AnnotatedInjectionMetadata(Class<?> targetClass, Collection<AnnotatedFieldElement> fieldElements,
                                          Collection<AnnotatedMethodElement> methodElements) {
            super(targetClass, combine(fieldElements, methodElements));
            this.fieldElements = fieldElements;
            this.methodElements = methodElements;
        }

        public Collection<AnnotatedFieldElement> getFieldElements() {
            return fieldElements;
        }

        public Collection<AnnotatedMethodElement> getMethodElements() {
            return methodElements;
        }
    }

    /**
     * {@link Annotation Annotated} {@link Method} {@link InjectionMetadata.InjectedElement}
     */
    private class AnnotatedMethodElement extends InjectionMetadata.InjectedElement {

        private final Method method;

        private final AnnotationAttributes attributes;

        private volatile Object object;

        protected AnnotatedMethodElement(Method method, PropertyDescriptor pd, AnnotationAttributes attributes) {
            super(method, pd);
            this.method = method;
            this.attributes = attributes;
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {

            Class<?> injectedType = pd.getPropertyType();
            Object injectedObject = getInjectedObject(attributes, bean, beanName, injectedType, this);
            ReflectionUtils.makeAccessible(method);
            method.invoke(bean, injectedObject);

        }

    }

    /**
     * {@link Annotation Annotated} {@link Field} {@link InjectionMetadata.InjectedElement}
     */
    public class AnnotatedFieldElement extends InjectionMetadata.InjectedElement {

        private final Field field;

        private final AnnotationAttributes attributes;

        private volatile Object bean;

        protected AnnotatedFieldElement(Field field, AnnotationAttributes attributes) {
            super(field, null);
            this.field = field;
            this.attributes = attributes;
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {

            Class<?> injectedType = resolveInjectedType(bean, field);
            Object injectedObject = getInjectedObject(attributes, bean, beanName, injectedType, this);
            ReflectionUtils.makeAccessible(field);
            field.set(bean, injectedObject);

        }

        private Class<?> resolveInjectedType(Object bean, Field field) {
            Type genericType = field.getGenericType();
            if (genericType instanceof Class) { // Just a normal Class
                return field.getType();
            } else { // GenericType
                return resolveTypeArgument(getTargetClass(bean), field.getDeclaringClass());
            }
        }
    }
}
