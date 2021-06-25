package com.jeeagile.dubbo.processor;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.core.env.PropertyResolver;

import java.lang.annotation.Annotation;
import java.util.Map;

import static com.alibaba.spring.util.AnnotationUtils.getAttributes;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
class AgileDubboPropertyValuesAdapter implements PropertyValues {

    private final PropertyValues delegate;

    /**
     * @param attributes
     * @param propertyResolver
     * @param ignoreAttributeNames
     */
    public AgileDubboPropertyValuesAdapter(Map<String, Object> attributes, PropertyResolver propertyResolver,
                                           String... ignoreAttributeNames) {
        this.delegate = new MutablePropertyValues(getAttributes(attributes, propertyResolver, ignoreAttributeNames));
    }

    public AgileDubboPropertyValuesAdapter(Annotation annotation, PropertyResolver propertyResolver,
                                           boolean ignoreDefaultValue, String... ignoreAttributeNames) {
        this.delegate = new MutablePropertyValues(getAttributes(annotation, propertyResolver, ignoreDefaultValue, ignoreAttributeNames));
    }

    public AgileDubboPropertyValuesAdapter(Annotation annotation, PropertyResolver propertyResolver, String... ignoreAttributeNames) {
        this(annotation, propertyResolver, true, ignoreAttributeNames);
    }

    @Override
    public PropertyValue[] getPropertyValues() {
        return delegate.getPropertyValues();
    }

    @Override
    public PropertyValue getPropertyValue(String propertyName) {
        return delegate.getPropertyValue(propertyName);
    }

    @Override
    public PropertyValues changesSince(PropertyValues old) {
        return delegate.changesSince(old);
    }

    @Override
    public boolean contains(String propertyName) {
        return delegate.contains(propertyName);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }
}
