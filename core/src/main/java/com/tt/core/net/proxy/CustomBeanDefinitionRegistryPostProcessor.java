package com.tt.core.net.proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * 提前注入
 */
public class CustomBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private final Class<?>[] clzArr;
    private final Class<?> factoryBeanClz;
    private final Object[] args;

    public CustomBeanDefinitionRegistryPostProcessor(Class<?>[] clzArr, Class<?> factoryBeanClz, Object... args) {
        this.clzArr = clzArr;
        this.factoryBeanClz = factoryBeanClz;
        this.args = args;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (Class<?> clz : clzArr) {
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition()
                    .getBeanDefinition();
            beanDefinition.setBeanClass(factoryBeanClz);
            ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            constructorArgumentValues.addGenericArgumentValue(clz);
            for (Object arg : args) {
                constructorArgumentValues.addGenericArgumentValue(arg);
            }
            beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
            beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

            registry.registerBeanDefinition(clz.getSimpleName() + "SendProxy", beanDefinition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
