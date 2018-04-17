package com.faker.netty.core.common;

import com.faker.netty.annotation.Controller;
import com.faker.netty.core.parser.ControllerParser;
import com.faker.netty.core.parser.SpringControllerParser;
import com.faker.netty.model.MethodMetaData;
import com.faker.netty.spring.SpringService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by faker on 18/4/16.
 */
@Component(value = "controllerRegistryBean")
public class ControllerRegistryBean implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private ApplicationContext ctx;

    private ControllerParser parser;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // 需要被代理的接口
        SpringControllerParser springControllerParser = new SpringControllerParser(ctx);
        List<Class> controllerClassList = springControllerParser.getControllerList();
        parser = springControllerParser;
        for (Class clz : controllerClassList) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clz);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            definition.setBeanClass(clz);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            // 注册bean名,一般为类名首字母小写
            beanDefinitionRegistry.registerBeanDefinition(clz.getSimpleName(), definition);
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    public ControllerParser getParser() {
        return parser;
    }

}